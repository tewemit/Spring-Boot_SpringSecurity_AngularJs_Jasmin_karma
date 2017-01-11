
package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import demo.models.Faq;
import demo.models.User;
import demo.models.UserDao;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static config.CsrfRequestPostProcessor.csrf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK)
@SpringBootTest(classes = {demo.Application.class, demo.controllers.FaqController.class,demo.controllers.UserController.class, demo.models.Faq.class,demo.models.FaqDao.class})
//@WebMvcTest(value = demo.controllers.FaqController.class)
/*
@EntityScan
@ComponentScan(basePackages = {"demo.controllers","demo.models","demo.config","demo.persistent","demo.services"})
//@ComponentScan({"demo.config","demo.controllers","demo.models","demo.repository","demo.services"})
//@ComponentScan ({"demo.models.Faq.class","demo.models.User.class","demo.models.UserDao.class","demo.models.FaqDao.class","demo.controllers.UserController.class",
//		"demo.services.UserDetailService.class","demo.controllers.FaqController.class"})
@WebAppConfiguration
@ContextConfiguration
//@RestClientTest
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class})
//@WebMvcTest(demo.controllers.FaqController.class)
*/
@AutoConfigureMockMvc
@WebAppConfiguration
public class AppControllersTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));
	//to Test Spring MockMvc Security Config using Base64 basic authentication. (username:password)
	String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64(("user:user").getBytes()));

	private static Gson gson = new Gson();

	@Autowired
	private MockMvc mockMvc;
	// the webapplication context setup is usable if there is a running webserver
	@Autowired
	private WebApplicationContext webApplicationContext;

	private String userName = "user";

	private HttpMessageConverter mappingJackson2HttpMessageConverter;


	private List<Faq> faqs = new ArrayList<>();

	@Mock
	private UserDao userdao;

	@Before
	public void initTests() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	/**
	 * Test if list Faq's works as expected
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void getFaqs() throws Exception {

		List<Faq> faq = new ArrayList<>() ;


		this.mockMvc.perform(get("/faqs").header("Authorization", basicDigestHeaderValue))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)))
		;
	}

	/**
	 * Test if adding FAQ's works as expected
	 * @throws Exception
	 */

	@Test
	@Transactional
	public void createFaqs() throws Exception {

		 Faq faq = new Faq();
		faq.setCategory("cat4");
		faq.setQuestion("question4");
		faq.setAnswer("Answer4");
		String faqJson = gson.toJson(faq);
		//this.mockMvc.perform(post("/saveFaq")
		MvcResult result1 =this.mockMvc.perform(post("/addFaq")
				.header("Authorization", basicDigestHeaderValue)
				//.contentType(MediaType.APPLICATION_JSON_UTF8)
				//.accept(MediaType.APPLICATION_JSON)
				.param("category",faq.getCategory())
				.param("question",faq.getQuestion())
				.param("answer",faq.getAnswer())
				//.param("_csrf","X-XSRF-TOKEN")
				.with(csrf())
				//.content(faqJson)
				)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
		ObjectMapper mapper = new ObjectMapper();
		Faq faq2 = new Faq();
		faq2 = mapper.readValue(result1.getResponse().getContentAsString(),Faq.class);
		Long id = faq2.getId();


	}



	/**
	 * Test user CRUD operations
	 * @throws Exception
	 */
	@Test
	public void shouldCreateRetrieveDelete() throws Exception {
		User user1 = mockUser("shouldCreateRetrieveDelete");
		byte[] user1Json = toJson(user1);

		//CREATE
		MvcResult result = mockMvc.perform(post("/createUser")
				.content(user1Json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				//.andExpect(redirectedUrlPattern(RESOURCE_LOCATION_PATTERN ))
				.andReturn();
		long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

		//RETRIEVE
		mockMvc.perform(get("/userById?id="+id)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is((int) id)))
				/*
				.andExpect(jsonPath("$.name", is(user1.getName())))
				.andExpect(jsonPath("$.username", is(user1.getUsername())))
				.andExpect(jsonPath("$.password", is(user1.getPassword())))
				.andExpect(jsonPath("$.email", is(user1.getEmail())));
				*/
				;
		//DELETE
		MvcResult result2 = mockMvc.perform(delete("/deleteuser?id="+id))

				.andExpect(status().isNoContent())
				.andReturn();

		//RETRIEVE should fail
		mockMvc.perform(get("/userById?id="+id )
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());


		//todo: we can test the 404 error body too.

	}


	@Test
	public void shouldCreateAndUpdateAndDelete() throws Exception {
		User user1 = mockUser("shouldCreateAndUpdate");
		byte[] r1Json = toJson(user1);
		//CREATE
		MvcResult result = mockMvc.perform(post("/createUser")
				.content(r1Json)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				// .andExpect(redirectedUrlPattern(RESOURCE_LOCATION_PATTERN))
				.andReturn();
		long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

		User user2 = mockUser("shouldCreateAndUpdate2");
		user2.setId(id);
		byte[] user2Json = toJson(user2);

		//UPDATE
		result = mockMvc.perform(put("/update")
						.content(user2Json)
						.contentType(MediaType.APPLICATION_JSON)
				//.accept(MediaType.APPLICATION_JSON)
		)
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();

		//RETRIEVE updated
		mockMvc.perform(get("/userById?id=" + id)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is((int) id)))
				/*.andExpect(jsonPath("$.name", is(user2.getName())))
				.andExpect(jsonPath("$.password", is(user2.getPassword())))
				.andExpect(jsonPath("$.email", is(user2.getEmail())))
				.andExpect(jsonPath("$.username", is(user2.getUpdateDate())));
				*/
				;
		//DELETE
		mockMvc.perform(delete("/deleteuser?id=" + id))
				.andExpect(status().isNoContent());
	}



    /*
     ******************************
     */

	private long getResourceIdFromUrl(String locationUrl) {
		String[] parts = locationUrl.split("/");
		return Long.valueOf(parts[parts.length - 1]);
	}


	private User mockUser(String prefix) {
		User r = new User();
		r.setName(prefix + "_name");
		r.setUsername(prefix + "_username");
		r.setPassword(prefix + "_password");
		r.setName(prefix + "_name");
		r.setEmail(prefix + "_test@test.com");
		return r;
	}

	private byte[] toJson(Object r) throws Exception {
		ObjectMapper map = new ObjectMapper();
		return map.writeValueAsString(r).getBytes();
	}

	// match redirect header URL (aka Location header)
	private static ResultMatcher redirectedUrlPattern(final String expectedUrlPattern) {
		return new ResultMatcher() {
			public void match(MvcResult result) {
				Pattern pattern = Pattern.compile("\\A" + expectedUrlPattern + "\\z");
				assertTrue(pattern.matcher(result.getResponse().getRedirectedUrl()).find());
			}
		};
	}
}

