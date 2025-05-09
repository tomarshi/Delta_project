import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ATM {

    
    static class Account {
        private String accountNumber;
        private String pin;
        private double balance;

        public Account(String accountNumber, String pin, double balance) {
            this.accountNumber = accountNumber;
            this.pin = pin;
            this.balance = balance;
        }

        public boolean validatePin(String inputPin) {
            return this.pin.equals(inputPin);
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
            }
        }

        public boolean withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                return true;
            } else {
                return false;
            }
        }
    }

    private static Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        accounts.put("123456", new Account("123456", "1234", 1000.0));
        accounts.put("987654", new Account("987654", "4321", 2000.0));

        System.out.println("Welcome to the ATM!");

        boolean mainExit = false;
        while (!mainExit) {
            System.out.println("\n1. Add Account");
            System.out.println("2. Access Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int mainChoice = scanner.nextInt();
            scanner.nextLine();  

            switch (mainChoice) {
                case 1:
                    addAccount(scanner);
                    break;
                case 2:
                    accessAccount(scanner);
                    break;
                case 3:
                    mainExit = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }

        scanner.close();
    }

    private static void addAccount(Scanner scanner) {
        System.out.print("Enter new account number: ");
        String newAccountNumber = scanner.nextLine();
        if (accounts.containsKey(newAccountNumber)) {
            System.out.println("Account number already exists!");
            return;
        }
        
        System.out.print("Enter PIN: ");
        String newPin = scanner.nextLine();

        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine();  

        accounts.put(newAccountNumber, new Account(newAccountNumber, newPin, initialBalance));
        System.out.println("Account added successfully!");
    }

    private static void accessAccount(Scanner scanner) {
        System.out.print("Enter your account number: ");
        String accountNumber = scanner.nextLine();

        Account account = accounts.get(accountNumber);
        if (account != null) {
            System.out.print("Enter your PIN: ");
            String pin = scanner.nextLine();

            if (account.validatePin(pin)) {
                boolean exit = false;
                while (!exit) {
                    System.out.println("\n1. Check Balance");
                    System.out.println("2. Deposit");
                    System.out.println("3. Withdraw");
                    System.out.println("4. Exit");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            System.out.println("Your balance is: " + account.getBalance());
                            break;
                        case 2:
                            System.out.print("Enter amount to deposit: ");
                            double depositAmount = scanner.nextDouble();
                            account.deposit(depositAmount);
                            System.out.println("Deposited: " + depositAmount);
                            break;
                        case 3:
                            System.out.print("Enter amount to withdraw: ");
                            double withdrawAmount = scanner.nextDouble();
                            if (account.withdraw(withdrawAmount)) {
                                System.out.println("Withdrawn: " + withdrawAmount);
                            } else {
                                System.out.println("Insufficient balance!");
                            }
                            break;
                        case 4:
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid option!");
                            break;
                    }
                }
            } else {
                System.out.println("Invalid PIN!");
            }
        } else {
            System.out.println("Account not found!");
        }
    }
}
