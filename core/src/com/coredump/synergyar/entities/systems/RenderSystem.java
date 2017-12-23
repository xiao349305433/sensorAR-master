package com.coredump.synergyar.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.coredump.synergyar.ar.PerspectiveAR;
import com.coredump.synergyar.entities.components.Model3DComponent;
import com.coredump.synergyar.entities.components.Position3DComponent;

import java.util.Comparator;

/**
 * @author fabio
 * @version 0.0.1
 * @since 0.0.1
 * Created by fabio on 11/18/15.
 */
public class RenderSystem extends IteratingSystem {
    private final static String TAG = RenderSystem.class.getName();

    private ComponentMapper<Model3DComponent> mModel3DMapper;
    private ComponentMapper<Position3DComponent> mPosition3DMapper;
    private Array<Entity> mRenderQueue;
    Array<ModelInstance> mInstances;
    private final PerspectiveAR mCamera;
    private final CameraInputController mController;
    private final ModelBatch mBatch;
    private Environment mEnvironment;
    private Comparator<Entity> mComparator;

    private boolean mIsLoading;

    //private ComponentMapper<Geolocation> mPositions = ComponentMapper.getFor(Geolocation.class);

    public RenderSystem(PerspectiveAR camera, CameraInputController controller,
                        Comparator<Entity> comparator) {
        super(Family.all(Position3DComponent.class, Model3DComponent.class).get());
        mModel3DMapper = ComponentMapper.getFor(Model3DComponent.class);
        mPosition3DMapper = ComponentMapper.getFor(Position3DComponent.class);
        //Might be encapsulated inside the PerspectiveAR class
        mCamera = camera;
        Gdx.app.log(TAG,mCamera.toString()+" "+mCamera.near);
        mController = controller;
        mBatch = new ModelBatch();
        mEnvironment = new Environment();
        mIsLoading = true;
        mInstances = new Array<ModelInstance>();
        mRenderQueue = new Array<Entity>();
        mComparator = comparator;
        mEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        mEnvironment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    private void loadInstances() {
        Gdx.app.log(TAG, "Loading instances");
        ModelInstance instance = null;
        Gdx.app.log(TAG, mRenderQueue.size+"");
        //Sorts the entities
        if(mRenderQueue.size > 1) {
            mRenderQueue.sort(mComparator);
        }
        for(Entity entity: mRenderQueue) {
            Model3DComponent model3D = mModel3DMapper.get(entity);
            Position3DComponent position = mPosition3DMapper.get(entity);
            instance = new ModelInstance(model3D.model);
            mInstances.add(instance);
        }
        Gdx.app.log(TAG, mInstances.size+"");
        mIsLoading = false;
    }
    //NO ESTA MOSTRANDO CAMBIOS EN LA PERSPECTIVA
    @Override
    public void update(float deltaTime) {
        Gdx.app.log(TAG, "updating system");
        super.update(deltaTime);
        if (mIsLoading) {
            loadInstances();
        }
        mCamera.render();
        mBatch.begin(mCamera);
        mBatch.render(mInstances, mEnvironment);
        mBatch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Gdx.app.log(TAG, "Processing entity");
        mRenderQueue.add(entity);
    }
}
