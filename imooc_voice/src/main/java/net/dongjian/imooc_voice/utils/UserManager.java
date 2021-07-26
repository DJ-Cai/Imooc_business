package net.dongjian.imooc_voice.utils;

import net.dongjian.imooc_voice.model.user.User;

public class UserManager {

    private static volatile UserManager userManager;
    private User user;

    private UserManager(){}

    public static UserManager getInstance(){
        if(userManager == null){
            synchronized (UserManager.class){
                if (userManager == null){
                    userManager = new UserManager();
                }
            }
        }
        return userManager;
    }

    /**
     * 保存用户信息到内存
     * @param user
     */
    public void saveUser(User user){
        this.user = user;
    }

    /**
     *保存用户信息到本地数据库（持久化）
     * @param user
     */
    private void saveLocal(User user){

    }

    /**
     * 获取用户信息
     * @return
     */
    public User getUser(){
        return user;
    }

    /**
     * 从本地获取用户信息
     * @return
     */
    private User getUserLocal(){
        return null;
    }

    /**
     * 判断用户是否登录
     * @return
     */
    public boolean hasLogined(){
        return getUser() == null ? false : true;
    }

    public void removeUser(){
        user = null;
        removeUserLocal();
    }

    private void removeUserLocal() {
    }
}
