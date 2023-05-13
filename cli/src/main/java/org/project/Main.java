package org.project;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.project.model.Product;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = "webshop-cli", version = "1.0", mixinStandardHelpOptions = true, subcommands = {Main.CreateCommand.class, Main.ListCommand.class, Main.UpdateCommand.class, Main.DelCommand.class})
public class Main implements Runnable {
    static OkHttpClient client = new OkHttpClient();
    static ObjectMapper json = new ObjectMapper();
    static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    static String baseURL = "http://localhost:8080/api/product";

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("Welcome to Farmers Market Admin Panel\navailable commands are: \n  create,list,update,delete\nuse these commands to add,view,update and delete products on your shop.");
    }

    public static void printError(Response r) {
        switch (r.code()) {
            case 404:
                System.out.println("404: product not found");
                break;
            case 500:
                System.out.println("500: internal server error");
                break;
            default:
                System.out.println("unrecognised error code: " + r.code());
        }
    }

    @CommandLine.Command(name = "create", mixinStandardHelpOptions = true, description = "adds a new product to webshop")
    static
    class CreateCommand implements Runnable {
        @CommandLine.Option(names = {"-n", "--name"}, description = "Product name.", required = true)
        String name;

        @CommandLine.Option(names = {"-p", "--price"}, description = "Product price (USD).", required = true)
        float price;

        @CommandLine.Option(names = {"-s", "--stock"}, description = "Number in Stock.", required = true)
        int inStock;

        @CommandLine.Option(names = {"-w", "--weight"}, description = "Product weight (Kg).", required = true)
        float weight;

        @CommandLine.Option(names = {"-i", "--image"}, description = "Product image file path (relative to static/ folder)", required = true)
        String img;


        @Override
        public void run() {
            try {
                Product newProduct = new Product(0, name, weight, inStock, price, img);
                RequestBody body = RequestBody.create(json.writeValueAsString(newProduct), JSON);
                Call call = client.newCall(new Request.Builder().url(baseURL).post(body).build());
                Response response = call.execute();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Product createdProduct = json.readValue(response.body().string(), Product.class);
                        System.out.printf("%s\nAbove product was successfully added to database.", createdProduct);
                    } else {
                        System.out.println("No product was created");
                    }
                } else  printError(response);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @CommandLine.Command(name = "list", mixinStandardHelpOptions = true, description = "lists products")
    static class ListCommand implements Runnable {
        @CommandLine.Option(names = {"-l", "--limit"}, description = "limits the output list.", defaultValue = "10")
        int limit = 10;


        @Override
        public void run() {
            Call call = client.newCall(new Request.Builder().url(baseURL).build());
            try (Response response = call.execute()) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Product> products = json.readValue(response.body().string(), new TypeReference<>() {
                        });

                        String formattedOutput = products.subList(0, Math.min(limit, products.size())).stream().map( p -> String.format("%s\n", p))
                                .collect(Collectors.joining());
                        System.out.println(formattedOutput);
                    } else {
                        System.out.println("No products were found");
                    }
                }  else  printError(response);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @CommandLine.Command(name = "delete", mixinStandardHelpOptions = true, description = "deletes a product given its id.")
    static class DelCommand implements Runnable {
        @CommandLine.Parameters(description = "id of the product being deleted.", paramLabel = "<id>")
        int id;


        @Override
        public void run() {
            Call call = client.newCall(new Request.Builder().url(baseURL + "/" + id).delete().build());
            try (Response response = call.execute()) {
                if (response.isSuccessful()) {
                    System.out.printf("Product #%d was successfully deleted from database.", id);
                }else  printError(response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @CommandLine.Command(name = "update", mixinStandardHelpOptions = true, description = "updates a product")
    static class UpdateCommand implements Runnable {

        @CommandLine.Parameters(description = "id of the product being updated.", paramLabel = "<id>")
        long id;

        @CommandLine.Option(names = {"-n", "--name"}, description = "Product name.")
        String name;

        @CommandLine.Option(names = {"-p", "--price"}, description = "Product price (USD).")
        float price = -1;

        @CommandLine.Option(names = {"-s", "--stock"}, description = "Number in Stock.")
        int inStock = -1;

        @CommandLine.Option(names = {"-w", "--weight"}, description = "Product weight (Kg).")
        float weight = -1;

        @CommandLine.Option(names = {"-i", "--image"}, description = "Product image file path (relative to static/ folder)")
        String img;


        @Override
        public void run() {
            try {
                Response getResponse = client.newCall(new Request.Builder().url(baseURL + "/" + id).build()).execute();
                if (getResponse.isSuccessful()) {
                    if (getResponse.body() != null) {
                        Product product = json.readValue(getResponse.body().string(), Product.class);
                        Product updatedProduct = new Product(product.getId(),
                                name != null ? name : product.getName(),
                                weight != -1 ? weight : product.getWeight(),
                                inStock != -1 ? inStock : product.getInStock(),
                                price != -1 ? price : product.getPrice(),
                                img != null ? img : product.getImg()
                        );
                        RequestBody body = RequestBody.create(json.writeValueAsString(updatedProduct), JSON);
                        Call call = client.newCall(new Request.Builder().url(baseURL).post(body).build());
                        Response response = call.execute();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Product updatedProductInDB = json.readValue(response.body().string(), Product.class);
                                System.out.printf("%s\nchanged to:\n%s", product,updatedProductInDB);
                            } else {
                                System.out.println("No product was updated");
                            }
                        }else  printError(response);

                    } else {
                        System.out.println("No products were found");
                    }
                } else  printError(getResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}