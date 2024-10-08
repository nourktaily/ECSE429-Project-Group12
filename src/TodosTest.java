import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class TodosTest {

    private final HttpClient client = HttpClient.newHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();
    public static String categoryId = "0";
    public static String taskId = "0";

    @BeforeAll
    public static void setup() throws IOException, InterruptedException {
        taskId = createTaskOfTodo();
    }
       public static String createTaskOfTodo() throws IOException, InterruptedException {
        // Initialize the HttpClient
           HttpClient client = HttpClient.newHttpClient();

        // Create the request body
        String requestBody = "{ \"Id\": \"1\" }";

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/tasksof"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request and capture the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert that the response status code is 201 Created
        assertEquals(201, response.statusCode());

        // Optionally, print the response body for debugging
        System.out.println(response.body());
        return "1";
    }


    public void createCategory() throws IOException, InterruptedException {
        String categoryRequestBody = "{ \"title\": \"Category Title\", \"description\": \"Category Description\" }";
        HttpRequest categoryRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/categories"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(categoryRequestBody))
                .build();

        HttpResponse<String> categoryResponse = client.send(categoryRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, categoryResponse.statusCode());

        // Extract the category ID from the response body
        String responseBody = categoryResponse.body();
        categoryId = objectMapper.readTree(responseBody).get("id").asText();
        System.out.println("Category Created with ID: " + categoryId);
    }
    @Test
    public void testGetAllTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testHeadAllTodos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testCreateTodoWithInvalidDoneStatus() throws IOException, InterruptedException {
        String requestBody = "{ \"title\": \"s aute irure dolor i\", \"doneStatus\": \"false\", \"description\": \"sse cillum dolore eu\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Check for expected error response
        assertEquals(400, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testCreateTodo() throws IOException, InterruptedException {
        String requestBody = "{ \"title\": \"s aute irure dolor i\", \"doneStatus\": false, \"description\": \"sse cillum dolore eu\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        System.out.println(response.body());
    }
    @Test
    public void testDeleteTodo() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/3"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }
    @Test
    public void testGetTodoById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1"))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testGetNonExistentTodo() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/999"))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testHeadTodoById() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testCreateTodoWithIncorrectId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/999"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("")) // Empty body
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        System.out.println(response.body());
    }
    @Test
    public void testUpdateTodoWithID() throws IOException, InterruptedException {
        String requestBody = "{ \"title\": \" new title \", \"doneStatus\": false, \"description\": \"new description\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Empty body
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testUpdateTodoDoneStatusOnly() throws IOException, InterruptedException {
        String requestBody = "{ \"doneStatus\": true }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/7"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testUpdateTodoWithAllFields() throws IOException, InterruptedException {
        String requestBody = "{ \"title\": \"Updated Title\", \"doneStatus\": false, \"description\": \"Updated Description\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }



    @Test
    public void testUpdateTodoTitle() throws IOException, InterruptedException {
        String requestBody = "{ \"title\": \"New Title\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testUpdateNonExistentTodo() throws IOException, InterruptedException {
        String requestBody = "{ \"title\": \"Title\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/999"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testGetCategoriesForTodo() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories"))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testHeadCategoriesForTodo() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
    }


    @Test
    public void testCreateLinkBetweenTodoAndCategory() throws IOException, InterruptedException {
        createCategory();
        String requestBody = "{ \"Id\": \"" + categoryId + "\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testCreateLinkWithInvalidCategoryId() throws IOException, InterruptedException {
        String requestBody = "{ \"Id\": \"999\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testDeleteLinkBetweenTodoAndCategory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories/" + categoryId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testDeleteNonExistentLinkBetweenTodoAndCategory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/categories/999"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testGetTasksOfTodo() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/tasksof"))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testHeadTasksOfTodo() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/tasksof"))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testCreateNewTaskOfTodo() throws IOException, InterruptedException {
        String requestBody = "{ \"Id\": \"1\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/tasksof"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testDeleteTaskOfTodo() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos/1/tasksof/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }



    @Test
    public void testGetTodosByDoneStatusTrue() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos?doneStatus=true"))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }

    @Test
    public void testGetTodosByDoneStatusFalse() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567/todos?doneStatus=false"))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
    }
}
