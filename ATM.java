import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
      
        //init scanner
        Scanner sc = new Scanner(System.in);

        //init Bank
        Bank theBank = new Bank("State Bank of India");

        //add a user, which also creates a saveings account
        User aUser =  theBank.addUser("Vignesh", "V J", "9481");

        //add a checking account for our user 
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true){
            // stay in the login prompt untill succussful login
             curUser = ATM.mainMenuPrompt(theBank, sc);

            //stay in main menu until user quits
            ATM.printUserMenu(curUser, sc);
           
        }

    }

    public static User mainMenuPrompt(Bank theBank, Scanner sc) {

        //inits
        String userID;
        String pin;
        User authUser;

        //prompt the user for user ID/pin combo until a correct one is reached

        do { 
            System.out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();
            System.out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            //try to get user object corresponding to the ID and pin combo
            authUser = theBank.userLogin(userID, pin);
            if(authUser == null){
                System.out.println("Invalid user ID or pin." + "Please try again.");
            }
        } while (authUser == null); // continue looping unitl successful login

        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc){

        // print a summary the user's accounts
        theUser.printAccountSummary();

        //init
        int choice;

        // user menu
        do { 
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("Welcome %s, What would you like to do ?\n", theUser.getFirstName());
            System.out.println(" 1:- Show account transaction history");
            System.out.println(" 2:- Withdraw");
            System.out.println(" 3:- Deposit");
            System.out.println(" 4:- Transfer");
            System.out.println(" 5:- Quit");
            System.out.println();
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Enter choice: ");
            choice = sc.nextInt();
            
            if(choice < 1 || choice > 5) {
                System.out.println("Invalid Choice. Please Choose 1-5");
                continue;
            }
        // process the choice
        switch(choice) {

            
        case 1:
        ATM.showTransaction(theUser,sc);
        break;

        case 2:
        ATM.withdrawlFunds(theUser, sc);
        break;

        case 3:
        ATM.depositFunds(theUser, sc);
        break;
        
        case 4:
        ATM.transferFunds(theUser, sc);
        break;

        case 5:
        System.out.println("Thank you for using State Bank of India. Goodbye!");
        break; // Exit the loop and terminate
        }

        // redisplay this menu unless the user wants to quit
        if(choice !=5){
            ATM.printUserMenu(theUser, sc);
        }
    }while (choice!= 5);
}

    /**
     * Show the trasnaction for an account
     * @param theUser the logged-in user object
     * @param sc the Scanner object used for user input
     */
    public static void showTransaction(User theUser, Scanner sc) {
        int theAcct;

        //get the account whose transaction history to look at
        do { 
            System.out.printf("Enter the number (1-%d) of the account " +
                     "whose transactions you want to see: ", 
                      theUser.numAccounts());
            theAcct = sc.nextInt()-1;
            if(theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

        //print the transaction History
        theUser.printAcctTransHistory(theAcct);
    }

    /**
     * Process transfering funds from one account to another
     * @param theUser the logged in User object
     * @param sc the Scanner object used for user input
     */
    public static void transferFunds(User theUser, Scanner sc) {
        int fromAcct, toAcct;
        double amount, acctBal;
    
        // Get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                              "to transfer from: ", theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);
    
        // Get the account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                              "to transfer to: ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
    
        // Get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max $%.2f): ", acctBal);
            amount = sc.nextDouble();  // Changed from nextInt() to nextDouble()
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not exceed balance of $%.2f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
    
        // Perform the transfer
        theUser.addAcctTransaction(fromAcct, -1 * amount,
                String.format("Transfer to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount,
                String.format("Transfer from account %s", theUser.getAcctUUID(fromAcct)));
    }
    

    /**
     * Process a fund withdrawl from an account
     * @param theUser the logged-in user object
     * @param sc the Scanner object user for user input
     */
    public static void withdrawlFunds(User theUser, Scanner sc) {

        //inits
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        //get the account to transfer from
        do{
            System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from: ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if(fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");

            }
        } while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        //get the amount to transfer
        do { 
            System.out.printf("Enter the amount to transfer (max $%.02f): $",acctBal);
            amount = sc.nextInt();
            if(amount < 0){
                System.out.println("Amount must be greater that zero. ");
            }else if(amount > acctBal) {
                System.out.printf("Amount must be greater than" + "Balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        //gobbel up rest of previous input
        sc.nextLine();

        //get a memo
        System.out.print
        ("Enter a memo: ");
        memo = sc.nextLine();

        //do the withdrawl
        theUser.addAcctTransaction(fromAcct, -1*amount, memo);
    }

    /**
     * Process a fund deposit to an account
     * @param theUser the logged-in user object
     * @param sc the Scanner object used for user input
     */
    public static void depositFunds(User theUser, Scanner sc) {
        int toAcct;
        double amount;
        String memo;
    
        // Get the account to deposit into
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                              "to deposit in: ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
    
        // Get the amount to deposit
        do {
            System.out.print("Enter the amount to deposit (max $0.00): ");
            amount = sc.nextDouble();  // Changed from nextInt() to nextDouble()
            if (amount < 0) {
                System.out.println("Amount must be greater than zero.");
            }
        } while (amount < 0);
    
        // Consume leftover newline character
        sc.nextLine();
    
        // Get a memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();
    
        // Perform the deposit
        theUser.addAcctTransaction(toAcct, amount, memo);
    }    
}
