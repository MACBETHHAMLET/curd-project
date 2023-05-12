package org.project;

import picocli.CommandLine;

@CommandLine.Command(name = "webshop-cli", version = "1.0", mixinStandardHelpOptions = true)
public class Main implements Runnable {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {

    }

}

@CommandLine.Command(name = "create", mixinStandardHelpOptions = true, description = "adds a new product to webshop")
class CreateCommand implements Runnable{
    @CommandLine.Option(names = {"-n", "--name"},description = "Product name.", required = true)
    String name;

    @CommandLine.Option(names = {"-p", "--price"},description = "Product price (USD).", required = true)
    float price;

    @CommandLine.Option(names = {"-s", "--stock"},description = "Number in Stock.", required = true)
    int inStock;

    @CommandLine.Option(names = {"-w", "--weight"},description = "Product weight (Kg).", required = true)
    float weight;

    @CommandLine.Option(names = {"-i", "--image"},description = "Product image file path (relative to static/ folder)", required = true)
    String img;


    @Override
    public void run() {

    }
}


@CommandLine.Command(name = "list", mixinStandardHelpOptions = true, description = "lists products")
class ListCommand implements Runnable{
    @CommandLine.Option(names = {"-l", "--limit"},description = "limits the output list.", defaultValue = "10")
    int limit=10;


    @Override
    public void run() {

    }
}

@CommandLine.Command(name = "delete", mixinStandardHelpOptions = true, description = "deletes a product given its id.")
class DelCommand implements Runnable{
    @CommandLine.Parameters(description = "id of the product being deleted.", paramLabel = "<id>")
    int id;


    @Override
    public void run() {

    }
}


@CommandLine.Command(name = "update", mixinStandardHelpOptions = true, description = "updates a product")
class UpdateCommand implements Runnable{

    @CommandLine.Parameters(description = "id of the product being updated.",paramLabel = "<id>")
    long id;

    @CommandLine.Option(names = {"-n", "--name"},description = "Product name.")
    String name;

    @CommandLine.Option(names = {"-p", "--price"},description = "Product price (USD).")
    float price;

    @CommandLine.Option(names = {"-s", "--stock"},description = "Number in Stock.")
    int inStock;

    @CommandLine.Option(names = {"-w", "--weight"},description = "Product weight (Kg).")
    float weight;

    @CommandLine.Option(names = {"-i", "--image"},description = "Product image file path (relative to static/ folder)")
    String img;


    @Override
    public void run() {

    }
}
