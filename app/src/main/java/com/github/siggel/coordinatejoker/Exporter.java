/*
 * Copyright (c) 2018 by siggel <siggel-apps@gmx.de>
 *
 *     This file is part of Coordinate Joker.
 *
 *     Coordinate Joker is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Coordinate Joker is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Coordinate Joker.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.siggel.coordinatejoker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * abstract parent class for various exporters
 * requiring export() to be individually implemented in derived exporters
 * those implementations may use sendFileViaIntent() of this parent to perform the intent sending
 * part
 */
abstract class Exporter {

    final File tmpDir;
    /**
     * the app's main context required for sending intents, accessing resources etc.
     */
    final Context context;

    private final File sharedDir;
    /**
     * whether to use ACTION_VIEW or ACTION_SEND
     */
    private final Boolean useActionViewIntent;

    /**
     * constructor providing context and telling whether to send ACTION_VIEW or ACTION_SEND intent
     *
     * @param context             context to be used
     * @param useActionViewIntent Boolean whether to use ACTION_VIEW or ACTION_SEND
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    Exporter(Context context, Boolean useActionViewIntent) {
        this.context = context;
        this.useActionViewIntent = useActionViewIntent;
        tmpDir = new File(context.getFilesDir(), "tmp");
        tmpDir.mkdirs();
        // for being able to share a file via intent, shared dir must be contained in
        // project's res/xml/filepaths.xml
        sharedDir = new File(context.getFilesDir(), "shared");
        sharedDir.mkdirs();
    }

    /**
     * export interface definition
     *
     * @param waypoints waypoints to be exported
     */
    abstract void export(List<Point> waypoints);

    /**
     * send file via intent to another activity using fileprovider for granting access rights
     * depending on constructor parameter, the intent will be sent as ACTION_VIEW or ACTION_SEND
     *
     * @param file     file to be provided to the other activity
     * @param mimeType mimeType of the file
     */
    void sendFileViaIntent(File file, String mimeType) {
        try {
            // copy content to be shared to shared directory
            File out = new File(sharedDir, file.getName());
            writeContentToFile(out, new FileInputStream(file));

            Intent intent = new Intent();

            // for providing read access, provide it via FileProvider
            Uri sharedFileUri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    out);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (useActionViewIntent) {
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(sharedFileUri, mimeType);
            } else {
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, sharedFileUri);
                intent.setType(mimeType);
            }
            context.startActivity(intent);
        } catch (IOException e) {
            throw new ExportException(context.getString(R.string.string_file_operation_failed));
        } catch (Exception e) {
            throw new ExportException(context.getString(R.string.string_sending_intent_failed));
        }
    }

    void writeContentToFile(File file, String content) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes("UTF-8"));
            outputStream.close();
        } catch (IOException e) {
            throw new ExportException(context.getString(R.string.string_file_operation_failed));
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    void writeContentToFile(File file, InputStream content) {
        final int bufferSize = 1024;
        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[bufferSize];
            int numberOfBytesRead;
            while ((numberOfBytesRead = content.read(buffer, 0, bufferSize)) != -1) {
                out.write(buffer, 0, numberOfBytesRead);
            }
            out.close();
        } catch (IOException e) {
            throw new ExportException(context.getString(R.string.string_file_operation_failed));
        }
    }
}