package com.agilesekeri.asugar_api.email;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {
        String[] parts = s.split("@");
        if (parts.length != 2)
            return false;

        else if(parts[0].length() == 0)
            return false;

        else if (!parts[1].contains("."))
            return false;

        return true;
    }
}
