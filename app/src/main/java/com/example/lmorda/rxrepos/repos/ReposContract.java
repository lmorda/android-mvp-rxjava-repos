/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lmorda.rxrepos.repos;

import com.example.lmorda.rxrepos.BasePresenter;
import com.example.lmorda.rxrepos.BaseView;
import com.example.lmorda.rxrepos.data.Repo;

import java.util.List;

public interface ReposContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showRepos(List<Repo> repos);

        void showLoadingReposError(String error);

        void showNoRepos();

        void showAllFilterLabel();

        void showJavaFilterLabel();

        void showKotlinFilterLabel();

        void showFilteringPopUpMenu();

        boolean isActive();
    }


    interface Presenter extends BasePresenter {

        void loadRepos(boolean forceUpdate);

        void setFiltering(ReposFilterType requestType);

        void setSearchString(String searchString);

        ReposFilterType getFiltering();

    }
}
