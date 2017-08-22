/*
 * MIT License
 *
 * Copyright (c) 2017 leobert-lan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package thirdparty.leobert.pvselectorlib.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p><b>Package:</b> thirdparty.leobert.pvselectorlib.broadcast </p>
 * <p><b>Project:</b> PicSelectorDemo </p>
 * <p><b>Classname:</b> ActionListenedBroadCastReceiver </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2017/8/21.
 */

public final class ActionListenedBroadCastReceiver extends BroadcastReceiver {

    private final List<ActionConsumer> consumers = new ArrayList<>();

    public ActionListenedBroadCastReceiver(ActionConsumer... actionConsumers) {
        if (actionConsumers == null || actionConsumers.length == 0)
            throw new IllegalStateException("must provide consumers");
        for (ActionConsumer consumer : actionConsumers) {
            if (consumer != null)
                consumers.add(consumer);
        }
    }

    public ActionListenedBroadCastReceiver(List<ActionConsumer> actionConsumers) {
        if (actionConsumers == null || actionConsumers.size() == 0)
            throw new IllegalStateException("must provide consumers");
        for (ActionConsumer consumer : actionConsumers) {
            if (consumer != null)
                consumers.add(consumer);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        for (ActionConsumer consumer : consumers) {
            if (consumer.getListenedAction().equals(action))
                consumer.consume(context, intent);
        }
    }

    public IntentFilter genIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        for (int i = 0; i < consumers.size(); i++) {
            intentFilter.addAction(consumers.get(i).getListenedAction());
        }
        return intentFilter;
    }

    public void registe(Context context) {
        context.registerReceiver(this, genIntentFilter());
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }
}
