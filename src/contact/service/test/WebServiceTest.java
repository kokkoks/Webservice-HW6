package contact.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.eclipse.jetty.client.api.Request;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

import contact.JettyMain;

public class WebServiceTest {
	private static String serviceUrl;
	private static HttpClient client;
	@BeforeClass
	public static void doFirst( ) throws Exception {
		serviceUrl = JettyMain.startServer( 8080 );
		client = new HttpClient();
		try {
			client.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	@AfterClass
	public static void doLast( ) throws Exception {
		// stop the Jetty server after the last test
		JettyMain.stopServer();
	}


	/**
	 * Test Success GET.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws java.util.concurrent.TimeoutException 
	 * @throws TieoutException
	 */
	@Test
	public void testGetSuccess() throws InterruptedException, ExecutionException, TimeoutException, java.util.concurrent.TimeoutException  {
		ContentResponse res = client.GET(serviceUrl+"contacts/1");
		assertEquals("Response should be 200 OK", Status.OK.getStatusCode(), res.getStatus());
		assertTrue("Content exist!", !res.getContentAsString().isEmpty());
	}

	/**
	 * Test Fail GET.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @throws java.util.concurrent.TimeoutException 
	 */
	@Test
	public void testGetFailure() throws InterruptedException, ExecutionException, TimeoutException, java.util.concurrent.TimeoutException {
		ContentResponse res = client.GET(serviceUrl+"contacts/0");
		assertEquals("The response should be 404 Not FOUND", Status.NOT_FOUND.getStatusCode(), res.getStatus());
		//				 assertTrue("Content does not exist", res.getContentAsString()==null);
	}

	/**
	 * Test success POST.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @throws java.util.concurrent.TimeoutException 
	 */
	@Test
	public void testPostSuccess() throws InterruptedException, ExecutionException, TimeoutException, java.util.concurrent.TimeoutException {
		StringContentProvider content = new StringContentProvider("<contact id=\"123\">" +
				"<title>RoboEarth</title>" +
				"<name>Earth Name</name>" +
				"<email>earth@email</email>" +
				"<phoneNumber>0000000000</phoneNumber>"+
				"</contact>");
		Request request = client.newRequest(serviceUrl+"contacts");
		request.method(HttpMethod.POST);
		request.content(content, "application/xml");
		ContentResponse res = request.send();

		assertEquals("POST complete ,should response 201 Created", Status.CREATED.getStatusCode(), res.getStatus());
		res = client.GET(serviceUrl+"contacts/123");
		assertTrue("Content Exist", !res.getContentAsString().isEmpty() );
	}

	/**
	 * Test Fail Post.
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws java.util.concurrent.TimeoutException 
	 */
	@Test
	public void testPostFailure() throws InterruptedException, TimeoutException, ExecutionException, java.util.concurrent.TimeoutException {
		StringContentProvider content = new StringContentProvider("");
		Request request = client.newRequest(serviceUrl+"contacts");
		request.method(HttpMethod.POST);
		request.content(content, "application/xml");
		ContentResponse res = request.send();

		assertEquals("This contact is null", Status.BAD_REQUEST.getStatusCode(), res.getStatus());
	}

	/**
	 * Test success PUT
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws java.util.concurrent.TimeoutException 
	 */
	@Test
	public void testPutSuccess() throws InterruptedException, TimeoutException, ExecutionException, java.util.concurrent.TimeoutException {
		StringContentProvider content = new StringContentProvider("<contact id=\"123\">" +
				"<title>UPDATE Title</title>" +
				"<name>UPDATE Name</name>" +
				"<email>update@email</email>" +
				"<phoneNumber>0123456789</phoneNumber>"+
				"</contact>");
		Request request = client.newRequest(serviceUrl+"contacts/1");
		request.method(HttpMethod.PUT);
		request.content(content, "application/xml");
		ContentResponse res = request.send();

		assertEquals("PUT Success should response 200 OK", Status.OK.getStatusCode(), res.getStatus());
	}

	/**
	 * Test Fail PUT
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws java.util.concurrent.TimeoutException 
	 */
	@Test
	public void testPutFailure() throws InterruptedException, TimeoutException, ExecutionException, java.util.concurrent.TimeoutException {
		StringContentProvider content = new StringContentProvider("<contact id=\"1234\">" +
				"<title>UPDATE Title</title>" +
				"<name>UPDATE Name</name>" +
				"<email>update@email</email>" +
				"<phoneNumber>0123456789</phoneNumber>"+
				"</contact>");
		Request request = client.newRequest(serviceUrl+"contacts/123");
		request.method(HttpMethod.PUT);
		request.content(content, "application/xml");
		ContentResponse res = request.send();

		assertEquals("PUT Fail should response 400 BAD REQUEST", Status.BAD_REQUEST.getStatusCode(), res.getStatus());
	}

	/**
	 * Test success DELETE
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @throws java.util.concurrent.TimeoutException 
	 */
	@Test
	public void testDeleteSuccess() throws InterruptedException, ExecutionException, TimeoutException, java.util.concurrent.TimeoutException {
		Request request = client.newRequest(serviceUrl+"contacts/3");
		request.method(HttpMethod.DELETE);
		ContentResponse res = request.send();

		assertEquals("DELETE success should response 200 OK", Status.OK.getStatusCode(), res.getStatus());
		res = client.GET(serviceUrl+"contacts/3");
		// assertTrue("Contact deleted", res.getContentAsString().isEmpty());
	}

	/**
	 * Test fail DELETE
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws java.util.concurrent.TimeoutException 
	 */
	@Test
	public void testDeleteFailure() throws InterruptedException, TimeoutException, ExecutionException, java.util.concurrent.TimeoutException {
		Request request = client.newRequest(serviceUrl+"contacts/3335");
		request.method(HttpMethod.DELETE);
		ContentResponse res = request.send();

		assertEquals("DELETE success should response 200 OK", Status.OK.getStatusCode(), res.getStatus());
	}
}
