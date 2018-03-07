/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match the package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * global variables
     */
    int coffeeQuantity = 1;
    int coffeePrice = 5;
    int extraWhippedCreamPrice = 0;
    int extraChocolatePrice = 0;
    String extraWhippedCream;
    String extraChocolate;
    String priceMessage;
    String newline = System.getProperty("line.separator");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // NAME:
        EditText textFieldName = (EditText) findViewById(R.id.textfield_name);
        // Convert it to a string
        String orderName = textFieldName.getText().toString();

        // CHECK FOR WHIPPED CREAM:
        CheckBox checkboxWhippedCream = (CheckBox) findViewById(R.id.checkbox_whipped_cream);
        // Call a method to find out if the checkboxes are checked
        boolean hasWhippedCream = checkboxWhippedCream.isChecked();
        // Change true/false response to yes/no in preferred language
        if (hasWhippedCream) {
            extraWhippedCream = getString(R.string.yes_please);
        } else {
            extraWhippedCream = getString(R.string.no_thanks);
        }
        // Log.v("MainActivity", "Has whipped cream: " + hasWhippedCream);

        // CHECK FOR CHOCOLATE:
        CheckBox checkboxChocolate = (CheckBox) findViewById(R.id.checkbox_chocolate);
        // Call a method to find out if the checkboxes are checked
        boolean hasChocolate = checkboxChocolate.isChecked();
        // Change true/false response to yes/no in preferred language
        if (hasChocolate) {
            extraChocolate = getString(R.string.yes_please);
        } else {
            extraChocolate = getString(R.string.no_thanks);
        }

        // GET PRICE:
        int price = calculatePrice(hasWhippedCream, hasChocolate);

        // DISPLAY MESSAGE ON SCREEN:
        String priceMessage = createOrderSummary(orderName, price, extraWhippedCream, extraChocolate);
        displayMessage(priceMessage);

        // SEND EMAIL:
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:olivia@yummy-wakame.com")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject, orderName));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Create summary of the order to print to screen and email
     * @param name of the person placing the order
     * @param price of the order
     * @param extraWhippedCream is whether or not the user wants whipped cream topping
     * @param extraChocolate is whether or not the user wants chocolate
     * @return text summary
     */
    private String createOrderSummary(String name, int price, String extraWhippedCream, String extraChocolate) {
        priceMessage = getString(R.string.email_name, name) + newline;
        priceMessage += getString(R.string.coffee_quantity, coffeeQuantity) + newline;
        priceMessage += getString(R.string.add_whipped_cream, extraWhippedCream) + newline;
        priceMessage += getString(R.string.add_chocolate, extraChocolate) + newline;
        // Format the price to currency
        priceMessage += getString(R.string.total, NumberFormat.getCurrencyInstance().format(price)) + newline;
        priceMessage += getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * Calculates the price of the order.
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        if (hasWhippedCream) {
            extraWhippedCreamPrice = 2;
        } else {
            extraWhippedCreamPrice = 0;
        }
        if (hasChocolate) {
            extraChocolatePrice = 2;
        } else {
            extraChocolatePrice = 0;
        }
        return (coffeePrice + extraWhippedCreamPrice + extraChocolatePrice) * coffeeQuantity;
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        coffeeQuantity = coffeeQuantity + 1;
        if (coffeeQuantity > 100) {
            coffeeQuantity = 100;
            // You cannot have more than 100 cups of coffee
            Toast.makeText(getApplicationContext(), getString(R.string.too_many_coffees), Toast.LENGTH_SHORT).show();
        }
        displayQuantity(coffeeQuantity);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        coffeeQuantity = coffeeQuantity - 1;
        if(coffeeQuantity < 1) {
            coffeeQuantity = 1;
            // You cannot have less than 1 cups of coffee
            Toast.makeText(getApplicationContext(), getString(R.string.too_few_coffees), Toast.LENGTH_SHORT).show();
        }
        displayQuantity(coffeeQuantity);
    }

    /**
     * This method displays the given coffeeQuantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        // By putting TextView in parentheses, we are casting the whole findViewByID()
        // which is a View into a TextView so that it is valid.
        // Then we can store that in the variable on the LHS which is also a type TextView.
        TextView order_summary_text_view = (TextView) findViewById(R.id.order_summary_text_view);
        // Now we can call TextView methods for this object like setText.
        // Otherwise setText would not be an option.
        // There is no setText for a View object, only for a TextView.
        order_summary_text_view.setText(message);
    }
}