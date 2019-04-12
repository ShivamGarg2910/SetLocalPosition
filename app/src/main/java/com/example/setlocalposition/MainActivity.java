package com.example.setlocalposition;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.core.Plane;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    ArFragment arFragment;
    ModelRenderable lampPostRenderable;
    ViewRenderable rendee;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        ModelRenderable.builder()
                .setSource(this, Uri.parse("Picture frame.sfb"))
                .build()
                .thenAccept(renderable -> lampPostRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });


        arFragment.setOnTapArPlaneListener(
                (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                    if (lampPostRenderable == null){
                        return;
                    }

                    Anchor anchor = hitresult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setLocalPosition(Vector3.zero());
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    arFragment.getPlaneDiscoveryController().hide();
                    arFragment.getPlaneDiscoveryController().setInstructionView(null);
                    //TransformableNode lamp = new TransformableNode(arFragment.getTransformationSystem());
                    //Node[] lamp = new Node[2];
                    /*ArrayList<Node> lamp;
                    lamp.add()
                    lamp[0].setParent(anchorNode);
                    lamp[0].setLocalPosition(new Vector3(-15f,2f,-2f));
                    lamp[0].setRenderable(lampPostRenderable);
                    //Node lamp[1]=new Node();
                    lamp[1].setParent(lamp[0]);
                    lamp[1].setLocalPosition(new Vector3(1f,-1f,1f));
                    lamp[1].setRenderable(lampPostRenderable);*/
                    //ArrayList<Node> frame1;
                    int x=-1;
                    int z=1;
                    for (int i=0;i<2;i++){
                        Node temp=new Node();
                        if(i==1)
                        {
                            temp.setLocalPosition(new Vector3(1.5f+x,1f,-5f+z));
                        }
                        else {
                            temp.setLocalPosition(new Vector3(1.5f, 0f, 5f));
                        }
                        temp.setParent(anchorNode);
                        temp.setRenderable(lampPostRenderable);
                    }

                    /*Node infoCard= new Node();
                    infoCard.setParent(lamp);
                    infoCard.setLocalPosition(new Vector3(0f,0.1f,0f));
                    infoCard.setRenderable(lampPostRenderable);
                    ViewRenderable.builder()
                            .setView(this,R.layout.textview1)
                            .build()
                            .thenAccept(
                                    (renderable)->{
                                        infoCard.setRenderable(rendee);
                                        TextView textView=(TextView) rendee.getView();
                                        textView.setText("Hello Owrl");
                                    }
                            );*/
                }
        );
    }
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
