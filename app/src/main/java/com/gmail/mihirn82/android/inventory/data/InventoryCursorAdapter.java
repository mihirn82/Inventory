package com.gmail.mihirn82.android.inventory.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.mihirn82.android.inventory.R;
import com.gmail.mihirn82.android.inventory.data.InventoryContract.InventoryEntry;


/**
 * Created by mihirnewalkar on 1/2/17.
 */

public class InventoryCursorAdapter extends CursorAdapter{

    /**
     * Constructs a new {@link InventoryCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        ImageView imageView = (ImageView) view.findViewById(R.id.product_image);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        final Button saleButton = (Button) view.findViewById(R.id.sale_button);

        // Find the columns of product attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int imageColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_IMAGE);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);

        // Extract properties from cursor
        String productName = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME));
        byte[] byteImage = cursor.getBlob(cursor.getColumnIndex(InventoryEntry.COLUMN_IMAGE));
        int price = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE));
        String dollarPrice = "$" + price;
        final int quantity = cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY));
        String sQuantity = "Quantity: " + quantity;

        // Populate fields with extracted properties
        nameTextView.setText(productName);

        Bitmap image = getImage(byteImage);
        imageView.setImageBitmap(image);

        priceTextView.setText(dollarPrice);
        quantityTextView.setText(sQuantity);
        saleButton.setText(R.string.button_text);
        saleButton.setTag(cursor.getPosition());

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = quantity;
                int position = (int) saleButton.getTag();
                Log.v("InventoryCursorAdapter","I clicked on sale button at position = " + position);

                if (qty > 0) {
                    qty = qty - 1;
                    Uri currentProductUri = ContentUris.withAppendedId (InventoryEntry.CONTENT_URI,position + 1);
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_QUANTITY, qty);

                    int rowsAffected = context.getContentResolver().update(currentProductUri, values, null, null);
                    if (rowsAffected == 0) {
                        // If the new content URI is null, then there was an error with insertion.
                        Log.v("InventoryCursorAdapter","failed " + currentProductUri);
                    }
                    else {
                        // Otherwise, the insertion was successful.
                        Log.v("InventoryCursorAdapter","successful " + currentProductUri);
                    }
                }
                else
                {
                    Toast.makeText(context,"No more items to sell", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
