package com.rupeng.service;

import org.springframework.stereotype.Service;

import com.rupeng.pojo.Setting;

@Service
public class SettingService extends BaseService<Setting> {
/**
 * 根据name值得到value
 * @param name
 * @return
 */
	public String getValueByName(String name) {
		// TODO Auto-generated method stub
		Setting setting = new Setting();
		setting.setName(name);
		setting= selectOne(setting);
		return setting.getValue();
	}

}
