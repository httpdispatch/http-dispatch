package net.customware.http.dispatch.server.standard;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.customware.http.dispatch.server.Dispatch;
import net.customware.http.dispatch.shared.Action;
import net.customware.http.dispatch.shared.DispatchException;
import net.customware.http.dispatch.shared.Result;
import net.customware.http.dispatch.shared.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStandardDispatchServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse res)
			throws ServletException, IOException
	{

		Throwable t = null;
		byte[] output = null;
		try
		{
			Object result = null;
			try
			{

				log.debug("trying to extract action from request");

				BufferedInputStream bis = new BufferedInputStream(
						request.getInputStream());
				ObjectInputStream ois = new ObjectInputStream(
						bis);
				result = getResult(ois, request);
				ois.close();
				log.debug("created response result, sending it back");

			} catch (DispatchException e)
			{
				log.debug("generated dispatch exception, sending it back");
				result = e;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			{
				oos.writeObject(result);
			}
			output = baos.toByteArray();

			log.debug("sent response back...");
		} catch (Throwable e)
		{
			log.error("Unable to process request", e);
			t = e;
		}
		processOutput(res, t, output);

	}

	@SuppressWarnings("unchecked")
	protected Object getResult(ObjectInputStream ois,
			HttpServletRequest request) throws IOException,
			ClassNotFoundException, DispatchException
	{
		Object result;
		Action<? extends Result> input = (Action<? extends Result>) ois
				.readObject();
		log.debug("got the action from the client:" + input);

		result = execute(input);
		return result;
	}

	protected void processOutput(HttpServletResponse res, Throwable t,
			byte[] output) throws IOException
	{
		ServletOutputStream sos = res.getOutputStream();

		if (output != null)
		{
			res.setContentType("application/octet-stream");
			res.setContentLength(output.length);
			sos.write(output);
		}
		else
		{
			res.setContentType("text/plain");
			byte[] message = t.getMessage().getBytes();
			res.setContentLength(message.length);
			sos.write(message);
		}

		sos.flush();
		sos.close();
	}

	private <R extends Result> R execute(Action<R> action)
			throws DispatchException
	{
		try
		{
			Dispatch dispatch = getDispatch();

			if (dispatch == null)
				throw new ServiceException("No dispatch found for servlet '"
						+ getServletName()
						+ "' . Please verify your server-side configuration.");

			return dispatch.execute(action);
		} catch (RuntimeException e)
		{
			log("Exception while executing " + action.getClass().getName()
					+ ": " + e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	/**
	 * 
	 * @return The Dispatch instance.
	 */
	protected abstract Dispatch getDispatch();

}
