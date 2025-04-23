package com.example.mobile_vocab_project.sync;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SyncWorker extends Worker{
    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        SyncUtils.syncRoomWithFireStore(getApplicationContext());
        Log.d("SyncWorker", "Sync triggered by WorkManager");
        return Result.success();
    }
}

