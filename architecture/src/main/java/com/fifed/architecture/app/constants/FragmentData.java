package com.fifed.architecture.app.constants;

import android.os.Bundle;

/**
 * Created by Fedir on 21.07.2016.
 */
public abstract class FragmentData {
   private Bundle bundle;

   public void setBundle(Bundle bundle) {
      this.bundle = bundle;
   }

   public Bundle getBundle() {
      return bundle;
   }

   public abstract Enum getFragmentID();
}
