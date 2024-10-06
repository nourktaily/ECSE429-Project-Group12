import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    private static final String BASE_URL = "http://localhost:4567/categories";
    private HttpClient client;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        client = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldRedirectToMainPage() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567"))
                .GET().build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(302, response.statusCode(), "Expected redirect from main page");
    }

    @Test
    public void shouldGetCategoriesStatus() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET().build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode(), "Expected status code 200 for /categories");
    }

    @Test
    public void shouldFetchCategories() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET().build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode(), "Expected status code 200 for GET /categories");
    }

    @Test
    public void shouldHandleHeadRequestOnCategories() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(URI.create(BASE_URL))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode(), "Expected status code 200 for HEAD /categories");
    }

    @Test
    public void shouldReturnBadRequestOnPostCategories() throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("description", "HELLO");
            put("id", "2");
            put("title", "Office");
        }};
        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Expected status code 400 for POST /categories with id");
    }

    @Test
    public void shouldReturnNotFoundForCategoryWithId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1"))
                .GET().build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response.statusCode(), "Expected status code 404 for GET /categories/1");
    }

    @Test
    public void shouldHandleHeadRequestOnCategoryId() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(URI.create(BASE_URL + "/1"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response.statusCode(), "Expected status code 404 for HEAD /categories/1");
    }

    @Test
    public void shouldReturnBadRequestOnPostCategoriesWithId() throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("description", "HELLO");
            put("id", "2");
            put("title", "Office");
        }};
        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/2"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Expected status code 400 for POST /categories/2");
    }

    @Test
    public void shouldReturnBadRequestOnPutCategoriesWithId() throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("description", "HELLO");
            put("id", "2");
            put("title", "Office");
        }};
        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/2"))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Expected status code 400 for PUT /categories/2");
    }

    @Test
    public void shouldDeleteCategoryWithId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Expected status code 200 for DELETE /categories/1");
    }

    @Test
    public void shouldReturnNotFoundForInvalidCategoryIds() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1"))
                .GET().build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response.statusCode(), "Expected status code 404 for GET /categories/1");
    }

    @Test
    public void shouldGetProjectsForCategoryWithId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1/projects"))
                .GET().build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode(), "Expected status code 200 for GET /categories/1/projects");
    }

    @Test
    public void shouldHandleHeadRequestOnCategoryIdProjects() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(URI.create(BASE_URL + "/1/projects"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode(), "Expected status code 200 for HEAD /categories/1/projects");
    }

    @Test
    public void shouldReturnNotFoundOnPostProjectsWithInvalidCategoryId() throws IOException, InterruptedException {
        var values = new HashMap<String, String>() {{
            put("description", "HELLO");
            put("id", "2");
            put("title", "Office");
        }};
        String requestBody = objectMapper.writeValueAsString(values);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/2/projects"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Expected status code 404 for POST /categories/2/projects");
    }

    @Test
    public void shouldReturnNotFoundOnDeleteProjectsWithInvalidCategoryId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1/projects/0"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Expected status code 404 for DELETE /categories/1/projects/0");
    }

    @Test
    public void shouldReturnNotFoundOnDeleteProjectWithInvalidCategoryId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1/projects/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Expected status code 404 for DELETE /categories/1/projects/1");
    }

    @Test
    public void shouldGetTodosForCategoryWithId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1/todos"))
                .GET()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode(), "Expected status code 200 for GET /categories/1/todos");
    }

    @Test
    public void shouldReturnNotFoundForTodosWithInvalidId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1/todos/1"))
                .GET()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response.statusCode(), "Expected status code 404 for GET /categories/1/todos/1");
    }

    @Test
    public void shouldReturnNotFoundOnDeleteTodosWithInvalidId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1/todos/1"))
                .DELETE()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(404, response.statusCode(), "Expected status code 404 for DELETE /categories/1/todos/1");
    }
}

