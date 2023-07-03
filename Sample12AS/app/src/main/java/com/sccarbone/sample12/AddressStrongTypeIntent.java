package com.sccarbone.sample12;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class AddressStrongTypeIntent 
{
  public String firstName;
  public String lastName;
  public String street;
  public String cityTown;
  public String state;
  public String zipCode;
  public String image;

  public enum ActionType
  { ADD,
    EDIT,
    DELETE
  }

  ActionType action;
  int addressIndex = 0;
  Intent intent;
  
  public AddressStrongTypeIntent(Intent intent)
  { Bundle bundle = intent.getExtras();
    try
    {
      firstName = bundle.getString("firstName");
      lastName = bundle.getString("lastName");
      street = bundle.getString("street");
      cityTown = bundle.getString("cityTown");
      state = bundle.getString("state");
      zipCode = bundle.getString("zipCode");
      image = bundle.getString("image");
      action = ActionType.values()[bundle.getInt("action",0)];
      addressIndex = bundle.getInt("addressindex");
    }
    catch(Exception ex)
    {    	
    }
  }

  public AddressStrongTypeIntent() 
  {
    firstName = "";
    lastName = "";
    street = "";
    cityTown = "";
    state = "";
    zipCode = "";
  }
  public AddressStrongTypeIntent(AddressAttributeGroup addressAttributes, ActionType action, int addressIndex) 
  {
    firstName = addressAttributes.firstName;
    lastName = addressAttributes.lastName;
    street = addressAttributes.street;
    cityTown = addressAttributes.cityTown;
    state = addressAttributes.state;
    zipCode = addressAttributes.zipCode;
    image = addressAttributes.image;
    this.action = action;
    this.addressIndex = addressIndex;
  }

  
  public void clearIntent()
  { intent = null;
	  
  }

  void putExtras()
  {
    intent.putExtra("firstName",firstName);
    intent.putExtra("lastName",lastName);
    intent.putExtra("street",street);
    intent.putExtra("cityTown",cityTown);
    intent.putExtra("state",state);
    intent.putExtra("zipCode",zipCode);
    intent.putExtra("image",image);
    intent.putExtra("action",action.ordinal());
    intent.putExtra("addressindex",addressIndex);
  }

  public Intent getIntent()
  {	if (intent == null)
    { intent = new Intent();
      putExtras();
    }
	return intent;
  }
  
  public Intent getIntent(Activity addressEntry,
          Class<AddressEntry> class1)
  {	if (intent == null)
    { intent = new Intent(addressEntry,class1);
      putExtras();
    }
	return intent;
  }

}
