package com.sccarbone.sample12;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddressEntry extends Activity implements View.OnClickListener 
{
  int CAMERA_PIC_REQUEST = 2;
  
  Button cmdSave; 
  Button cmdPhoto; 
  Button cmdCancel; 
  ImageView imagePhoto;
  EditText editFirstName;
  EditText editLastName;
  EditText editStreet;
  EditText editCityTown;
  EditText editState;
  EditText editZipCode;
  int result;
  AddressStrongTypeIntent stIntent;
  
  String imagePath = "";

  void showInfo(String message)
  {  Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }  
  
  
  @Override
  public void onCreate(Bundle savedInstanceState) 
  { super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_address_entry);
    editFirstName = (EditText) findViewById(R.id.editFirstName);
    editLastName = (EditText) findViewById(R.id.editLastName);
    editStreet = (EditText) findViewById(R.id.editStreet);
    editCityTown = (EditText) findViewById(R.id.editCityTown);
    editState = (EditText) findViewById(R.id.editState);
    editZipCode = (EditText) findViewById(R.id.editZipCode);
    imagePhoto = (ImageView) findViewById(R.id.imagePhoto);
    cmdSave = (Button) findViewById(R.id.cmdSave);     
    cmdSave.setOnClickListener(this);  
    cmdPhoto = (Button) findViewById(R.id.cmdPhoto);     
    cmdPhoto.setOnClickListener(this);    
    cmdCancel = (Button) findViewById(R.id.cmdCancel);     
    cmdCancel.setOnClickListener(this);    
    stIntent = new AddressStrongTypeIntent( getIntent());
    editFirstName.setText(stIntent.firstName);
    editLastName.setText(stIntent.lastName);
    editStreet.setText(stIntent.street);
    editCityTown.setText(stIntent.cityTown);
    editState.setText(stIntent.state);
    editZipCode.setText(stIntent.zipCode);
    imagePath = stIntent.image;
    if (stIntent.action ==AddressStrongTypeIntent.ActionType.DELETE)
    	cmdSave.setText(R.string.delete);
    editFirstName.setEnabled(stIntent.action!=AddressStrongTypeIntent.ActionType.DELETE);
    editLastName.setEnabled(stIntent.action!=AddressStrongTypeIntent.ActionType.DELETE);
    editStreet.setEnabled(stIntent.action!=AddressStrongTypeIntent.ActionType.DELETE);
    editCityTown.setEnabled(stIntent.action!=AddressStrongTypeIntent.ActionType.DELETE);
    editState.setEnabled(stIntent.action!=AddressStrongTypeIntent.ActionType.DELETE);
    editZipCode.setEnabled(stIntent.action!=AddressStrongTypeIntent.ActionType.DELETE);
    loadImage();

  }

  String getImagePathName()
  { String rcode="";
    //rcode = this.getCacheDir()+"/"+imagePath;
    //rcode = this.getExternalFilesDir("")+"/"+imagePath;
    rcode = this.getDataDir()+"/"+imagePath;
    return rcode;
  }

  void loadImage()
  { if (imagePath == null || imagePath.length() == 0)
	  return;
    
 	Bitmap imageBitmap;
  	BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 3;
    imageBitmap = BitmapFactory.decodeFile(getImagePathName(), options);
    imagePhoto.setImageBitmap(imageBitmap);
  }
  
  @Override
  public void finish() 
  {	stIntent.clearIntent();
    stIntent.firstName = editFirstName.getText().toString();
    stIntent.lastName = editLastName.getText().toString();
    stIntent.street = editStreet.getText().toString();
    stIntent.cityTown = editCityTown.getText().toString();
    stIntent.state = editState.getText().toString();
    stIntent.zipCode = editZipCode.getText().toString();
    stIntent.image = imagePath;
	setResult(result, stIntent.getIntent());
	super.finish();
  }   
  
  public void onClick(View v) 
  { if(cmdSave.getId() == v.getId())
    { result = RESULT_OK;
      finish();
	  
    }
    if(cmdPhoto.getId() == v.getId())
    { Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
      imagePath = "Address" + System.currentTimeMillis()+".png";
      startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }
    if(cmdCancel.getId() == v.getId())
    { result = RESULT_CANCELED;
      finish();
	
    } 
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) 
  {
      super.onActivityResult(requestCode, resultCode, data);
      
      if( requestCode == CAMERA_PIC_REQUEST)
      { if (resultCode == Activity.RESULT_OK) 
        { try 
          { Bitmap imageBitmap= (Bitmap) data.getExtras().get("data");

            File file = new File(getImagePathName());
            file.createNewFile();
            try
            { ByteArrayOutputStream bos = new ByteArrayOutputStream();

              imageBitmap.compress(Bitmap.CompressFormat.PNG,100,bos);

              byte[] bitmapdata = bos.toByteArray();

              FileOutputStream fos = new FileOutputStream(file);
              fos.write(bitmapdata);
              fos.flush();
              fos.close();
            }
            catch (IOException e)
            { e.printStackTrace();
            }

            loadImage();
          } 
          catch (Exception e) 
          { showInfo("Picture not taken");
            e.printStackTrace();
            imagePath = "";
          }
        }
      }
      else 
      { showInfo("Picture not taken");
      }

  }
  
  
}



