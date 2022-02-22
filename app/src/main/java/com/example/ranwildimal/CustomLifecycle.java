package com.example.ranwildimal;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public class CustomLifecycle implements LifecycleOwner {

   private LifecycleRegistry lifecycleRegistry;

   public CustomLifecycle() {
       lifecycleRegistry = new LifecycleRegistry(this);
       lifecycleRegistry.markState(Lifecycle.State.CREATED);

   }

   public void doOnResume() {
       lifecycleRegistry.markState(Lifecycle.State.RESUMED);
   }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}
