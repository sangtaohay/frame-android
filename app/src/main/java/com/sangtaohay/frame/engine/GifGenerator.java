package com.sangtaohay.memestudio.engine;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.sangtaohay.memestudio.dto.GifFrame;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GifGenerator {

    public static List<Bitmap> extractFrames(File file) throws IOException {


        GifDecoder gifDecoder = new GifDecoder();
        gifDecoder.read(fullyReadFileToBytes(file));
        List<Bitmap> listBitmap = new ArrayList<>();
        Log.v("frameCount", String.valueOf(gifDecoder.frameCount));
        if(0 == gifDecoder.getFrameCount()){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            listBitmap.add(bitmap);
        }else {
            gifDecoder.advance();
            do {
                Log.v("getCurrentFrameIndex", String.valueOf(gifDecoder.getCurrentFrameIndex()));
                Bitmap bm = gifDecoder.getNextFrame();
                listBitmap.add(Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight()));
                gifDecoder.advance();

            }
            while (gifDecoder.getCurrentFrameIndex() < gifDecoder.frameCount - 1);
        }
        return listBitmap;
    }
    public static List<GifFrame> extractFramesWithDelay(File file) throws IOException {


        GifDecoder gifDecoder = new GifDecoder();
        gifDecoder.read(fullyReadFileToBytes(file));
        List<GifFrame> listBitmap = new ArrayList<>();
        Log.v("frameCount", String.valueOf(gifDecoder.frameCount));
        if(0 == gifDecoder.getFrameCount()){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            listBitmap.add(new GifFrame(bitmap, 0));

        }else {
            gifDecoder.advance();
            do {
                Log.v("getCurrentFrameIndex", String.valueOf(gifDecoder.getCurrentFrameIndex()));
                Bitmap bm = gifDecoder.getNextFrame();
                int delay = gifDecoder.getNextDelay();
                listBitmap.add(new GifFrame(Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight()), delay));
                gifDecoder.advance();

            }
            while (gifDecoder.getCurrentFrameIndex() < gifDecoder.frameCount - 1);
        }
        return listBitmap;
    }
    public static void combineGifFrames(Context context, List<Bitmap> frames){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay(80);
        encoder.setRepeat(0);
        encoder.start(bos);

        for(Bitmap bmp: frames) {
            encoder.addFrame(bmp);
            bmp.recycle();
        }

        encoder.finish();
        File root = context.getExternalFilesDir(null);
        File myDir = new File(root + "/");
        File file = new File(myDir, "output.gif");

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file.getAbsoluteFile());
            outputStream.write(bos.toByteArray());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public static void combineGifFramesDragon66(Context context, List<Bitmap> frames) {
        File root = context.getExternalFilesDir(null);
        File myDir = new File(root + "/");
        File file = new File(myDir, "output.gif");
        try {
            // True for dither. Will need more memory and CPU
            AnimatedGifWriter writer = new AnimatedGifWriter(false);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Bitmap bitmap; // Grab the Bitmap whatever way you can
            // Use -1 for both logical screen width and height to use the first frame dimension
            writer.prepareForWrite(os, -1, -1);
            for(Bitmap bmp: frames) {
                writer.writeFrame(os, bmp);
            }
            // Keep adding frame here
            writer.finishWrite(os);
            // And you are done!!!
            OutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            os.writeTo(fos);
            fos.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void combineGifFramesWithDelay(Context context, List<Bitmap> frames, int delay){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setDelay(delay);
        encoder.setRepeat(0);
        encoder.start(bos);

        for(Bitmap bmp: frames) {
            encoder.addFrame(bmp);
            bmp.recycle();
        }

        encoder.finish();
        File root = context.getExternalFilesDir(null);
        File myDir = new File(root + "/");
        File file = new File(myDir, "output.gif");

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file.getAbsoluteFile());
            outputStream.write(bos.toByteArray());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }
    private static byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);;
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }
}
