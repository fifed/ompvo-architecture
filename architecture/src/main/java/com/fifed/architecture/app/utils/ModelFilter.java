package com.fifed.architecture.app.utils;


import com.fifed.architecture.app.observers.ObserverActivity;
import com.fifed.architecture.datacontroller.interaction.core.Model;


/**
 * Created by Fedir on 01.07.2016.
 */
public class ModelFilter {
    public static boolean isObserverWorkingWithModel(ObserverActivity observer, Model model){
        Class<?>[] observerInterfaces = observer.getClass().getInterfaces();
        Class<?>[] modelIntefaces = model.getClass().getInterfaces();

        for (Class<?> observerInterface : observerInterfaces) {
            for (Class<?> modelInteface : modelIntefaces) {
                if (observerInterface == modelInteface) return true;
            }
        }
        return false;
    }
}
