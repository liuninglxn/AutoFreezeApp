package com.tignioj.freezeapp.backend.viewmodel.repo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.tignioj.freezeapp.backend.AppsDataBase;
import com.tignioj.freezeapp.backend.dao.AppsCategoryDao;
import com.tignioj.freezeapp.backend.entitys.AppsCategory;

import java.util.List;

public class AppsCategoryRepository {

    private static AppsCategoryRepository INSTANCE;
    LiveData<List<AppsCategory>> listLiveDataAppsCategory;


    public  static AppsCategoryRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppsCategoryRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppsCategoryRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    AppsCategoryDao appsCategoryDao;
    AppsDataBase dataBase;

    LiveData<List<AppsCategory>> listLiveDataAppsCategoryForSpinner;

    private AppsCategoryRepository(Context context) {
        dataBase = AppsDataBase.getDataBase(context);
        appsCategoryDao = dataBase.getAppsCategoryDao();
        this.listLiveDataAppsCategory = appsCategoryDao.getAllAppsCategoryLive();
        this.listLiveDataAppsCategoryForSpinner = appsCategoryDao.getAllAppsCategoryLive();
    }

    public LiveData<List<AppsCategory>> getListLiveDataAppsCategoryForSpinner() {
        return listLiveDataAppsCategoryForSpinner;
    }

    public LiveData<List<AppsCategory>> getListLiveDataAppsCategory() {
        return listLiveDataAppsCategory;
    }

    public void insertAppsCategory(AppsCategory[] appsCategories) {
        new InsertAsyncTask(appsCategoryDao).execute(appsCategories);
    }

    public void deleteAppsCategory(AppsCategory[] appsCategories) {
        new DeleteAsyncTask(appsCategoryDao).execute(appsCategories);
    }

    public void updateAppsCategory(AppsCategory[] appsCategories) {
        new UpdateAsyncTask(appsCategoryDao).execute(appsCategories);
    }

    public void deleteAllAppsCategory() {
        new DeleteAllAsyncTask(appsCategoryDao).execute();
    }

    public LiveData<List<AppsCategory>> getListLiveDataAppsCategoryById(long id) {
        return appsCategoryDao.getAllAppsCategoryLiveByCategoryId(id);
    }

    public List<AppsCategory> getAppsCategory() {
        return appsCategoryDao.getAllAppsCategory();
    }

    public AppsCategory getCategoryBaCategoryCategoryName(String categoryName) {
        return appsCategoryDao.getAppsCategoryByCategoryName(categoryName);
    }

    public LiveData<List<AppsCategory>> findAppCategorysLiveWithPattern(String pattern) {
        return appsCategoryDao.findAppCategorysLiveWithPattern("%" + pattern.trim() + "%");
    }


    public static class InsertAsyncTask extends AsyncTask<AppsCategory, Void, Void> {
        private AppsCategoryDao appsCategoryDao;

        public InsertAsyncTask(AppsCategoryDao appsCategoryDao) {
            this.appsCategoryDao = appsCategoryDao;
        }

        @Override
        protected Void doInBackground(AppsCategory... tasks) {
            appsCategoryDao.insertAppsCategory(tasks);
            return null;
        }
    }


    public static class UpdateAsyncTask extends AsyncTask<AppsCategory, Void, Void> {
        private AppsCategoryDao appsCategoryDao;

        public UpdateAsyncTask(AppsCategoryDao appsCategoryDao) {
            this.appsCategoryDao = appsCategoryDao;
        }

        @Override
        protected Void doInBackground(AppsCategory... tasks) {
            appsCategoryDao.updateAppsCategory(tasks);
            return null;
        }
    }

    public static class DeleteAsyncTask extends AsyncTask<AppsCategory, Void, Void> {
        private AppsCategoryDao appsCategoryDao;

        public DeleteAsyncTask(AppsCategoryDao appsCategoryDao) {
            this.appsCategoryDao = appsCategoryDao;
        }

        @Override
        protected Void doInBackground(AppsCategory... tasks) {
            appsCategoryDao.deleteAppsCategory(tasks);
            return null;
        }
    }

    public static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppsCategoryDao appsCategoryDao;

        public DeleteAllAsyncTask(AppsCategoryDao appsCategoryDao) {
            this.appsCategoryDao = appsCategoryDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appsCategoryDao.deleteAllAppsCategory();
            return null;
        }
    }
}
