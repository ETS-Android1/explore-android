package com.mentalab;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class FileGenerator {

    private final Context context;
    private final boolean overwrite;


    public FileGenerator(RecordSubscriber recordSubscriber) {
        this.context = recordSubscriber.getContext();
        this.overwrite = recordSubscriber.getOverwrite();
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Set<UriTopicBean> generateFiles(Uri directory, String filename) {
        //todo: handle filename, what if overwrite is true etc
        ContentValues metaDataExg = new ContentValues();
        metaDataExg.put(DISPLAY_NAME, filename + "_Exg");
        metaDataExg.put(MIME_TYPE, "text/csv");

        ContentValues metaDataOrn = new ContentValues();
        metaDataOrn.put(DISPLAY_NAME, filename + "_Orn");
        metaDataOrn.put(MIME_TYPE, "text/csv");

        ContentValues metaDataMarkers = new ContentValues();
        metaDataMarkers.put(DISPLAY_NAME, filename + "_Markers");
        metaDataMarkers.put(MIME_TYPE, "text/csv");

        ContentResolver resolver = context.getContentResolver();

        Set<UriTopicBean> createdUris = new HashSet<>();
        if (overwrite) { //todo: delete doesn't work
            deleteIfExists(directory, filename + "_Exg.csv");
            createdUris.add(new UriTopicBean(resolver.insert(directory, metaDataExg), MentalabEnums.Topics.ExG));
            deleteIfExists(directory, filename + "_Orn.csv");
            createdUris.add(new UriTopicBean(resolver.insert(directory, metaDataOrn), MentalabEnums.Topics.Orn));
            deleteIfExists(directory, filename + "_Markers.csv");
            createdUris.add(new UriTopicBean(resolver.insert(directory, metaDataMarkers), MentalabEnums.Topics.Marker));
        } else {
            createdUris.add(
                    new UriTopicBean(createNewFile(directory, filename, metaDataExg, MentalabEnums.Topics.ExG), MentalabEnums.Topics.ExG));
            createdUris.add(
                    new UriTopicBean(createNewFile(directory, filename, metaDataOrn, MentalabEnums.Topics.Orn), MentalabEnums.Topics.Orn));
            createdUris.add(
                    new UriTopicBean(createNewFile(directory, filename, metaDataMarkers, MentalabEnums.Topics.Marker), MentalabEnums.Topics.Marker));
        }
        return createdUris;
    }


    private Uri createNewFile(Uri directory, String filename, ContentValues metaData, MentalabEnums.Topics topic) {
        Uri location;
        int i = 1;
        while ((location = context.getContentResolver().insert(directory, metaData)) == null) {
            metaData.put(DISPLAY_NAME, filename + "(" + i + ")_" + topic.name());
            i++;
        }
        return location;
    }


    private void deleteIfExists(Uri directory, String filename) {
        final Uri fullPath = Uri.parse(directory.toString() + File.separator + filename);
        final boolean fileExists = checkIfUriExists(fullPath);
        if (fileExists) {
            context.getContentResolver().delete(fullPath, null, null);
        }
    }

    private boolean checkIfUriExists(Uri contentUri) {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(contentUri, null, null, null, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                String filePath = cur.getString(0);

                if (new File(filePath).exists()) {
                    cur.close();
                    return true;// do something if it exists
                } else {
                    cur.close();
                    return false;// File was not found
                }
            } else {
                cur.close();
                return false;// Uri was ok but no entry found.
            }
        } else {
            return false;// content Uri was invalid or some other error occurred
        }
    }
}
