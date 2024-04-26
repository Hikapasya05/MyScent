package com.hika.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.hika.common.base.BaseRecyclerViewAdapter
import com.hika.user.databinding.ItemSettingBinding
import com.hika.user.util.ProfileSetting
import com.hika.user.util.ProfileSettings

class ProfileSettingsAdapter(
    private val onItemPressed: (ProfileSetting) -> Unit
): BaseRecyclerViewAdapter<ItemSettingBinding, ProfileSettings>() {
    override fun inflateViewBinding(parent: ViewGroup): ItemSettingBinding {
        return ItemSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val diffUtilBuilder: (List<ProfileSettings>, List<ProfileSettings>) -> DiffUtil.Callback?
        get() = { _, _ -> null}

    override fun ItemSettingBinding.binds(data: ProfileSettings) {
        ivIcon.setImageResource(data.icon)
        tvTitle.text = data.title

        root.setOnClickListener { onItemPressed(data.setting) }
    }
}