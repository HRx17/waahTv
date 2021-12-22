package com.tv.waah.RetrofitFiles;

import java.util.List;

public class Language {
    String email;
     List<langs> languages;

    public Language(String email, List<langs> languages) {
        this.email = email;
        this.languages = languages;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<langs> getLanguages() {
        return languages;
    }

    public void setLanguages(List<langs> languages) {
        this.languages = languages;
    }
}
