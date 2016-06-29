

import android.graphics.Bitmap;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Handler;
import android.widget.ImageView;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPaint;
import com.sun.pdfview.decrypt.PDFAuthenticationFailureException;


import net.sf.andpdf.nio.ByteBuffer;
import net.sf.andpdf.refs.HardReference;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by Slaush on 22/06/2016.
 */

public class PDFImageView
{
    // Constantes ..
    private static final int STARTPAGE = 1;
    private static final float STARTZOOM = 1.0f;
    private static final float MIN_ZOOM = 0.25f;
    private static final float MAX_ZOOM = 3.0f;
    private static final float ZOOM_INCREMENT = 1.5f;

    // Atributos ..
    private String pdffilename;
    private PDFFile mPdfFile;
    private Handler handler;
    private int mPage;
    private float mZoom;
    private PDFPage mPdfPage;
    private Bitmap pageBitmap;
    private ImageView img;

    public PDFImageView(String file, ImageView img) throws Exception
    {
        PDFImage.sShowImages = true;
        PDFPaint.s_doAntiAlias = false;
        HardReference.sKeepCaches= false;

        handler = new Handler();
        if(img == null)
            throw new Exception("ERROR");
        else
            this.img = img;

        if (file == null)
            throw new Exception("ERROR");
        else
        pdffilename = file;

        mPage = STARTPAGE;
        mZoom = STARTZOOM;
        pageBitmap = null;
        setContent();

    }


    private void setContent() throws Exception
    {
        parsePDF(pdffilename);
        showPage(mPage, mZoom);
    }

    private void parsePDF(String filename) throws IOException
    {
        File f = new File(filename);
        long len = f.length();
        if (len == 0)
            throw  new PDFAuthenticationFailureException("Error");
        else
        {
            openFile(f);
        }
    }


    public void openFile(File file) throws IOException
    {
        // first open the file for random access
        RandomAccessFile raf = new RandomAccessFile(file, "r");

        // extract a file channel
        FileChannel channel = raf.getChannel();

        // now memory-map a byte-buffer
        ByteBuffer bb = ByteBuffer.NEW(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
        mPdfFile = new PDFFile(bb);
    }

    private void showPage(final int page, final float zoom) throws Exception
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                float height,width;
                //liberarBitmap();
                img.setImageBitmap(null);
                if (mPdfPage == null || mPdfPage.getPageNumber() != page)
                {
                    mPdfPage = mPdfFile.getPage(page, true);
                }

                if(img.getWidth() == 0 || img.getHeight() == 0)
                {
                    width = mPdfPage.getWidth();
                    height = mPdfPage.getHeight();
                }
                else
                {
                    width = img.getWidth();
                    height = img.getHeight();
                }

                System.out.println(width+"x"+height);
                
                pageBitmap =  crearBitmap(width, height, zoom);
                img.setImageBitmap(pageBitmap);
            }
        });

    }

    private Bitmap crearBitmap(float width, float height, float zoom)
    {
        Bitmap page = mPdfPage.getImage((int)(mPdfPage.getWidth()*zoom),
                (int)(mPdfPage.getHeight()*zoom),
                null, false, true);
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, page.getWidth(), page.getHeight()), new RectF(0, 0, width, height), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(page, 0, 0, page.getWidth(), page.getHeight(), m, true);
    }

    private void liberarBitmap()
    {
        if(pageBitmap != null  && !pageBitmap.isRecycled())
        {
            pageBitmap.recycle();
            pageBitmap = null;
        }
    }

    public void zoomIn() throws Exception
    {
        if (mPdfFile != null)
        {
            if (mZoom < MAX_ZOOM)
            {
                mZoom *= ZOOM_INCREMENT;

                if (mZoom > MAX_ZOOM)
                    mZoom = MAX_ZOOM;

                showPage(mPage, mZoom);
            }
        }
    }

    public void zoomOut() throws Exception
    {
        if (mPdfFile != null)
        {
            if (mZoom > MIN_ZOOM)
            {
                mZoom /= ZOOM_INCREMENT;

                if (mZoom < MIN_ZOOM)
                    mZoom = MIN_ZOOM;

                showPage(mPage, mZoom);
            }
        }
    }

    public void nextPage() throws Exception
    {
        if (mPdfFile != null && mPage < mPdfFile.getNumPages())
        {
            mPage += 1;
            showPage(mPage, mZoom);
        }
    }

    public void prevPage() throws Exception
    {
        if (mPdfFile != null && mPage > 1)
        {
            mPage -= 1;
            showPage(mPage, mZoom);
        }
    }

    public void gotoPage(int page) throws Exception
    {
        if (mPdfFile != null && page > 1 && page < mPdfFile.getNumPages())
        {
            mPage = page;
            showPage(mPage, mZoom);
        }
    }

    public int getPage()
    {
        return mPage;
    }

    public float getZoom()
    {
        return mZoom;
    }

    public Bitmap getBitmap()
    {
        return pageBitmap;
    }

}
