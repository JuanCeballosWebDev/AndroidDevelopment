package com.sccarbone.sample12;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;


public class AddressDatabaseHelper extends SQLiteOpenHelper
{
	public static final String TABLE_ADDRESS = "address";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_FIRST_NAME = "firstName";
	public static final String COLUMN_LAST_NAME = "lastName";
	public static final String COLUMN_STREET = "street";
	public static final String COLUMN_CITY_TOWN = "cityTown";
	public static final String COLUMN_STATE = "state";
	public static final String COLUMN_ZIP_CODE = "zipCode";
	public static final String COLUMN_IMAGE = "image";

	  private static final String DATABASE_NAME = "address.db";
	  private static final int DATABASE_VERSION = 1;

	// Database creation SQL statement
	private static final String DATABASE_CREATE_SQL = "create table "
			+ TABLE_ADDRESS  + "("
			+ COLUMN_ID      + " integer primary key autoincrement, "
			+ COLUMN_FIRST_NAME    + " text not null, "
			+ COLUMN_LAST_NAME   + " text not null, "
			+ COLUMN_STREET   + " text not null, "
			+ COLUMN_CITY_TOWN  + " text not null, "
			+ COLUMN_STATE  + " text not null, "
			+ COLUMN_ZIP_CODE + " text not null, "
			+ COLUMN_IMAGE + " text not null);";

	  public AddressDatabaseHelper(Context context) 
	  { super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) 
	  { database.execSQL(DATABASE_CREATE_SQL);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	  { // Check prior version to understand upgrade steps
		// Export data
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
	    onCreate(db);
	    // Import Data
	  }
}

class AddressDataSource 
{
	  // Database fields
	  private SQLiteDatabase database;
	  final private AddressDatabaseHelper dbHelper;
	  final private String[] allColumns = { AddressDatabaseHelper.COLUMN_ID,
			AddressDatabaseHelper.COLUMN_FIRST_NAME, AddressDatabaseHelper.COLUMN_LAST_NAME,
			AddressDatabaseHelper.COLUMN_STREET, AddressDatabaseHelper.COLUMN_CITY_TOWN,
			AddressDatabaseHelper.COLUMN_STATE,
			AddressDatabaseHelper.COLUMN_ZIP_CODE, AddressDatabaseHelper.COLUMN_IMAGE};


	AddressDataSource(Context context) {
	    dbHelper = new AddressDatabaseHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public long createAddress(AddressAttributeGroup address) 
	  { ContentValues values = new ContentValues();

		  values.put(AddressDatabaseHelper.COLUMN_FIRST_NAME, address.firstName);
		  values.put(AddressDatabaseHelper.COLUMN_LAST_NAME, address.lastName);
		  values.put(AddressDatabaseHelper.COLUMN_STREET, address.street);
		  values.put(AddressDatabaseHelper.COLUMN_CITY_TOWN, address.cityTown);
		  values.put(AddressDatabaseHelper.COLUMN_STATE, address.state);
		  values.put(AddressDatabaseHelper.COLUMN_ZIP_CODE, address.zipCode);
	      values.put(AddressDatabaseHelper.COLUMN_IMAGE, address.image);
	      long insertId = database.insert(AddressDatabaseHelper.TABLE_ADDRESS,
	    		                        null,
	                                    values);
	    return insertId;
	  }

	  public void deleteAddress(AddressAttributeGroup address) 
	  { long id = address.id;
	    database.delete(AddressDatabaseHelper.TABLE_ADDRESS, 
	    		        AddressDatabaseHelper.COLUMN_ID + " = " + id, null);
	  }

	  public AddressCollection getAllAddresses() throws Exception 
	  { AddressCollection addresses = new AddressCollection();

	    Cursor cursor = database.query(AddressDatabaseHelper.TABLE_ADDRESS,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) 
	    { addresses.addAddress(cursorToAddressAttributeGroup(cursor));
	      cursor.moveToNext();
	    }
	    cursor.close();
	    return addresses;
	  }

	private AddressAttributeGroup cursorToAddressAttributeGroup(Cursor cursor)
	{
		AddressAttributeGroup address = new AddressAttributeGroup(cursor.getInt(
				cursor.getColumnIndex(AddressDatabaseHelper.COLUMN_ID)),
				cursor.getString(cursor.getColumnIndex(AddressDatabaseHelper.COLUMN_FIRST_NAME)),
				cursor.getString(cursor.getColumnIndex(AddressDatabaseHelper.COLUMN_LAST_NAME)),
				cursor.getString(cursor.getColumnIndex(AddressDatabaseHelper.COLUMN_STREET)),
				cursor.getString(cursor.getColumnIndex(AddressDatabaseHelper.COLUMN_CITY_TOWN)),
				cursor.getString(cursor.getColumnIndex(AddressDatabaseHelper.COLUMN_STATE)),
				cursor.getString(cursor.getColumnIndex(AddressDatabaseHelper.COLUMN_ZIP_CODE)),
				cursor.getString(cursor.getColumnIndex(AddressDatabaseHelper.COLUMN_IMAGE)));

		return address;
	}


}
