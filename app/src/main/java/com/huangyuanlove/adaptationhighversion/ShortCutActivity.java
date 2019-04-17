package com.huangyuanlove.adaptationhighversion;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class ShortCutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_cut);
        findViewById(R.id.add_shortcut).setOnClickListener(this);
        findViewById(R.id.remove_shortcut).setOnClickListener(this);
        findViewById(R.id.update_shortcut).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_shortcut:
                addShortcut();
                break;
            case R.id.remove_shortcut:
                removeShortcut();
                    break;
            case R.id.update_shortcut:
                updateShortcut();
                break;
        }
    }

    private void updateShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            Intent intent = new Intent(ShortCutActivity.this,ShortCutActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(ShortCutActivity.this, "shortcut")
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                    .setShortLabel( getString( R.string.shortcut) +"new")
                    .setLongLabel(getString(R.string.shortcut_example) +"new")
                    .setIntent(intent)
                    .build();
            shortcutManager.updateShortcuts(Arrays.asList( new ShortcutInfo[]{ shortcut}));
        }
    }

    private void removeShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

            //动态添加并且在桌面创建了快捷方式，删除之前，先disable一下.  或者判断
            List<ShortcutInfo> shortcutInfos = shortcutManager.getPinnedShortcuts();

            for(ShortcutInfo si : shortcutInfos){
                if(si.getId().equals("shortcut")){
                    shortcutManager.disableShortcuts(Arrays.asList(new String[]{"shortcut"}));
                }
            }
            shortcutManager.removeDynamicShortcuts(Arrays.asList(new String[]{"shortcut"}));

            Toast.makeText(ShortCutActivity.this,"共" + shortcutInfos.size() +"个桌面快捷方式",Toast.LENGTH_SHORT).show();

        }
    }

    private void addShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            Intent intent = new Intent(ShortCutActivity.this,ShortCutActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(ShortCutActivity.this, "shortcut")
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                    .setShortLabel( getString( R.string.shortcut))
                    .setLongLabel(getString(R.string.shortcut_example))
                    .setIntent(intent)
                    .build();
            shortcutManager.addDynamicShortcuts(Arrays.asList( new ShortcutInfo[]{ shortcut}));
        }
    }
}
