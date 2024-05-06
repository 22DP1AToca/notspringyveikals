import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    // Class with all the main screen functions -> input, input checking and error message output is here

    //Bank accounts
    BankAccount annasBank;
    BankAccount alisesBank;
    BankAccount janisBank;
    ArrayList<BankAccount> allBankAccounts;

    // Clases
    ConsoleController console;
    Scanner scanner1;
    ShoppingCart cart;
    FileFunc file;
    SignIn signIn;

    // User
    User loggedInUser;
    
    // List of all products in store
    ArrayList<Product> list;
    
    // Filter
    boolean filterChocolate;
    boolean filterJelly;
    boolean filterLolly;

    // Sort
    boolean sortNameA;
    boolean sortNameD;
    boolean sortPriceA;
    boolean sortPriceD;

    // Constructor -> sets up everything for work
    public App() throws FileNotFoundException, IOException{

        // Bank Account creation
        annasBank = new BankAccount(1000, "ANNA", "31010912", "106");
        alisesBank = new BankAccount(300, "ALISE", "13030720", "231");
        janisBank = new BankAccount(5, "JANIS", "18208970", "543");
        allBankAccounts = new ArrayList<BankAccount>();

        // Adds bank accounts to list
        allBankAccounts.add(annasBank);
        allBankAccounts.add(alisesBank);
        allBankAccounts.add(janisBank);

        console = new ConsoleController();
        scanner1 = new Scanner(System.in);
        cart = new ShoppingCart(); //Create shopping cart
        file = new FileFunc(PathFile.PRODUCTS.getFileName());
        signIn = new SignIn();

        //Save all products from CSV
        list = new ArrayList<Product>();
        list = file.productsObj;

        //Set default values for filter and sorter
        filterChocolate = true;
        filterJelly = true;
        filterLolly = true;
    
        sortNameA = false;
        sortNameD = false;
        sortPriceA = false;
        sortPriceD = false;

        //Get products from CSV
        file.GetAll();
    }

    // works the LogIn page
    public void LogInCode() throws FileNotFoundException, IOException{
        
        while(true){
            console.clearAll();
            console.SignInScreen();
            System.out.println("LOG IN {L} or REGISTER {R}");
            System.out.print("\nYour choice: ");
            String signInUserChoice = scanner1.next();

            switch (signInUserChoice) { // makes sure user enters a valid input
                case "L": 
                    signIn.LogIn();
                    loggedInUser = signIn.loggedinUser;
                    MainCode();
                    break;

                case "R":
                    signIn.Register();
                    loggedInUser = signIn.loggedinUser;
                    MainCode();
                    break;

                default:
                    System.out.print("Invalid input, try again");
                    break;
            }
            
        }
    }

    // works the Main page
    private void MainCode() throws FileNotFoundException, IOException{

        while(true){
            //Clear terminal
            console.clearAll();

            //Re-print terminal
            console.MainScreen(loggedInUser, list, filterChocolate, filterJelly, filterLolly, sortNameA, sortNameD, sortPriceA, sortPriceD);

            //Read input
            System.out.print("Enter your choice:");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            
            switch(input){
                //Filter UI
                case "F":
                    System.out.print("Filter chocolates {C}, jellies {J}, lollies {L}:");
                    String filterInput = scanner.next();
                    
                    Filter filter = new Filter();
                   

                    switch(filterInput){
                        case "C":
                            filterChocolate = !filterChocolate;
                            break;
                        case "J":
                            filterJelly = !filterJelly;
                            break;
                        case "L":
                            filterLolly = !filterLolly;
                            break;
                    }
                    
                    list = filter.FilterList(file.productsObj, filterChocolate, filterJelly, filterLolly);
                    continue;
                //Sorter UI
                case "SO":
                    sortNameA = false;
                    sortNameD = false;
                    sortPriceA = false;
                    sortPriceD = false;

                    System.out.print("Sort by name A-Z {NA}, name Z-A {ND}, price < {PA}, price > {PD}:");
                    String sortInput = scanner.next();

                    Sorter sorter = new Sorter();

                    switch(sortInput){
                        case "NA":
                            list = sorter.SortName(list, false);
                            sortNameA = true;
                            break;
                        case "ND":
                            list = sorter.SortName(list, true);
                            sortNameD = true;
                            break;
                        case "PA":
                            list = sorter.SortPrice(list, false);
                            sortPriceA = true;
                            break;
                        case "PD":
                            list = sorter.SortPrice(list, true);
                            sortPriceD = true;
                            break;
                    }
                    continue;
                //Search UI
                case "S":
                    while (true) {
                        Searcher searcher = new Searcher();
                        System.out.print("Search:");
                        String keyword = scanner.next();  // Read user input
                        
                        list = searcher.Search(file.productsObj, keyword);

                        //Clear terminal
                        console.clearAll();

                        //Re-print terminal
                        console.MainScreen(loggedInUser, list, filterChocolate, filterJelly, filterLolly, sortNameA, sortNameD, sortPriceA, sortPriceD);

                        System.out.println("Search:" + keyword);
                        System.out.print("Search again{A}, quit searching{Q}:");
                        String searchInput = scanner.next();

                        switch(searchInput){
                            case "A":
                                //Clear terminal
                                console.clearAll();

                                //Re-print terminal
                                console.MainScreen(loggedInUser, list, filterChocolate, filterJelly, filterLolly, sortNameA, sortNameD, sortPriceA, sortPriceD);
                                continue;
                            case "Q":
                                break;
                        }
                        break; //while break
                    }
                    continue; //continue code
                //Shopping cart UI
                case "SC":
                    ShoppingCartCode();
                    break;
                //Product UI
                case "P":
                    boolean whileLoop = true;
                    while(whileLoop){
                        scanner.useDelimiter("[,\\s+]"); // Used to make [Press Enter to continue] possible
                        System.out.print("Input product {ID}:");

                        if (scanner.hasNextInt()){

                            int ID = scanner.nextInt();
                            for (Product temp : list) {
                                if(temp.ID == ID){
                                    whileLoop = false;
                                    temp.selectedStatus = !temp.selectedStatus;    
                                    if(temp.selectedStatus){
                                        if (!cart.AddTo(temp)){
                                            System.out.print("Sorry, this item is out of stock [Press Enter to continue]"); 
                                            scanner.next();
                                            temp.selectedStatus = false;
                                        } 
                                    }else{
                                        cart.RemoveFrom(temp);
                                    }                                
                                }
                            }
                            console.clearOneLine(22 + list.size());

                        } else {
                            scanner.next();
                            console.clearOneLine(22 + list.size());
                            continue;
                        }                       
                    }
                    continue;
                case "E":
                    System.exit(0);
                    break;

                default:
                    // Wrong input;
                    break;
            }                
        }
    }

    // works the Shopping Cart page
    private void ShoppingCartCode() throws FileNotFoundException, IOException{
        
        // Shopping cart
        boolean firstWhileloop = true;
        while(firstWhileloop){
            console.clearAll();
            console.ShoppingCartScreen(cart);
            
            System.out.print("Your choice: ");
            String SCUserChoice = scanner1.next();

            switch (SCUserChoice) { // makes sure user enters a valid input
                case "E":  //Exit program
                    System.exit(0);
                    break;

                case "M": // Gose back to main page
                    MainCode();
                    break;

                //Product UI
                case "P":
                    Product product = null;
                    while(product == null){ // makes sure user enters a an existing products ID
                        System.out.print("Input the product you wan to edit {ID}:");
                        if (scanner1.hasNextInt()){ // makes sure user enters a valid integer input
                            int ID = scanner1.nextInt();
                            for (Product temp : cart.cartProducts.keySet()) {
                                if(temp.ID == ID){
                                    product = temp;
                                }          
                            }
                            console.clearOneLine(16 + cart.cartProducts.size());
                        } else{
                            scanner1.next();
                            console.clearOneLine(16 + cart.cartProducts.size());
                            continue;
                        }
                        
                    }

                    boolean thirdWhileLoop = true;
                    while(thirdWhileLoop){ // makes sure user enters a valid input
                        console.clearOneLine(16 + cart.cartProducts.size());
                        System.out.print("REMOVE {R} this product or CHANGE AMOUNT {CA}: ");
                        String userRorCA = scanner1.next();

                        switch (userRorCA) {
                            //remove product UI
                            case "R":
                                cart.RemoveFrom(product);
                                product.selectedStatus = false; // de-selects the product
                                thirdWhileLoop = false;
                                break;
                        
                            //Change product amount UI
                            case "CA":
                                boolean forthWhileLoop = true;
                                while(forthWhileLoop){ // makes sure user enters a valid input
                                    console.clearOneLine(17 + cart.cartProducts.size());
                                    System.out.print("Enter the new amount: ");

                                    if (scanner1.hasNextInt()){ // makes sure user enters a valid integer input
                                        int amount = scanner1.nextInt();
                                    
                                        if (!cart.SetAmount(product, amount)){ // checks if setting the amount is posible, if true - sets amount to input
                                            scanner1.useDelimiter("[,\\s+]"); // Used to make [Press Enter to continue] possible
                                            System.out.print("Sorry, there isn't enough of this producs in the storage, try smaller amount [Press Enter to continue]"); 
                                            scanner1.next();
                                        }
                                        forthWhileLoop = false;
                                        break;
                                    } else {
                                        scanner1.next();
                                        console.clearOneLine(17 + cart.cartProducts.size());
                                        continue;
                                    }
                                }
                                thirdWhileLoop = false;

                            default:
                                //Wrong input
                                break;
                        }
                    }
                    break;

                //Order UI
                case "O": 

                    if (cart.cartProducts.size() == 0){
                        System.out.print("\nYour cart is empty {Press Enter to continue}");
                        scanner1.useDelimiter("[,\\s+]"); // Used to make [Press Enter to continue] possible
                        scanner1.next();
                        // ShoppingCartCode();
                        break;
                    }

                    BankAccount bankAcc = null;

                    //Bank account information fill in UI
                    while(bankAcc == null){ // repeats till user fills in all of the fiels correctly or till exists this UI
                        console.clearAll();
                        console.ShoppingCartScreen(cart);

                        System.out.println("Please enter your information");
                        System.out.println("-----------------------------");
                        System.out.println("Bank account number: ");
                        System.out.println("Bank/Cards owner's name (IN CAPS): ");
                        System.out.println("CVV: ");

                        console.clearOneLine(17 + cart.cartProducts.size());
                        System.out.print("Bank account number: ");
                        String userBankAccNumber = scanner1.next();

                        console.clearOneLine(18 + cart.cartProducts.size());
                        System.out.print("Bank/Cards owner's name (IN CAPS): ");
                        String userOwner = scanner1.next();

                        console.clearOneLine(19 + cart.cartProducts.size());
                        System.out.print("CVV: ");
                        String userCVV = scanner1.next();

                        for (BankAccount tempBankAcc : allBankAccounts){ // Cheks if bankAcc with this infomation exists -> if exists, sets it as the one in use
                            if (userBankAccNumber.equals(tempBankAcc.GetBankAccNumber()) && userOwner.equals(tempBankAcc.GetOwner()) && userCVV.equals(tempBankAcc.GetCVV())){
                                bankAcc = tempBankAcc;
                                if (cart.OrderCart(loggedInUser, bankAcc, list)){ // Orders cart (if transation is successful)
                                    System.out.print("\nThank you for shopping with us! Your order has been placed and you can view your check. :)");
                                    System.exit(0);// Exists the shop

                                } else { // Error -> if transation wasnt successful
                                    scanner1.useDelimiter("[,\\s+]"); // Used to make [Press Enter to continue] possible
                                    System.out.print("\nSomething went wrong in the transaction, please try again! [Press Enter to continue]");
                                    scanner1.next();
                                    ShoppingCartCode();
                                }   
                            }
                        }

                        boolean whileLoop = true;
                        while(whileLoop){ // makes sure user enters a valid input

                            console.clearOneLine(20 + cart.cartProducts.size());
                            System.out.print("There is a mistake in your information. Do you want to Try again {T}, Continue shopping {C} or Exit the shop {E}: ");
                            String userTorCorE = scanner1.next();

                            switch (userTorCorE) {
                                case "T":
                                    whileLoop = false;
                                    break;

                                case "C":
                                    whileLoop = false;
                                    ShoppingCartCode();
                                    break;

                                case "E":
                                    whileLoop = false;
                                    System.out.print("\nThank you for shopping with us!");
                                    System.exit(0);                                    
                                    break;
                            
                                default:
                                    break;
                            }
                        }
                    }                   
                    break;

                default:
                    System.out.print("Invalid input, try again");
                    break;
            }
        }
    }
}