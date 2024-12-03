package Classes;

import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;

import Helpers.ErrorHandler;

public class Data {

	public static JSONArray getData(File file, Container parent) {
        String jsonTxt = null;
		try {
			jsonTxt = IOUtils.toString(new FileReader(file));
		} catch (FileNotFoundException e) {
			new ErrorHandler( file.getPath()+" not found", null);
		} catch (IOException e) {
			new ErrorHandler("Could not read " + file.toString(), null);
		}
		JSONArray arr = new JSONArray(jsonTxt);
		return arr;
	}
}
