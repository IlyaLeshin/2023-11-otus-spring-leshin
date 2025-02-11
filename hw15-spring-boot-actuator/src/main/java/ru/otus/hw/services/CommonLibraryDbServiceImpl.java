package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.hw.repositories.CommonLibraryDbRepository;

@Service
@AllArgsConstructor
public class CommonLibraryDbServiceImpl implements CommonLibraryDbService {

    private final CommonLibraryDbRepository commonLibraryDbRepository;

    @Override
    public boolean checkAccessToDb() {
        try {
            commonLibraryDbRepository.executeTestQuery();
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
