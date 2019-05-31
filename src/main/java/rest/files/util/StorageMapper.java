package rest.files.util;

import rest.files.model.UserFile;
import rest.files.to.FileTo;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class StorageMapper {

    public static FileTo toTo(UserFile file) {
        return new FileTo(file.getName(), file.getPath());
    }

    public static FileTo toPathOnlyTo(UserFile file) {
        return new FileTo(null, file.getPath());
    }

    public static List<FileTo> toListTo(List<UserFile> files) {
        return files.stream().map(StorageMapper::toTo).collect(toList());
    }

    public static List<FileTo> toPathOnlyListTo(List<UserFile> files) {
        return files.stream().map(StorageMapper::toPathOnlyTo).collect(toList());
    }
}
