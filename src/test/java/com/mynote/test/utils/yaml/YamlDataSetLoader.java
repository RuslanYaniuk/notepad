package com.mynote.test.utils.yaml;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class YamlDataSetLoader {

    public static IDataSet load(final String file) throws IOException, DataSetException {
        IDataSet dataSet;

        if (isYamlEmpty(file)) {
            throw new FileNotFoundException("File is empty");

        } else {
            final InputStream inputStream = Thread.currentThread().getClass().getResourceAsStream(file);

            dataSet = new YamlDataSet(inputStream);

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return dataSet;
    }

    private static boolean isYamlEmpty(final String yamlFile) throws IOException {
        final InputStream inputStream = Thread.currentThread().getClass().getResourceAsStream(yamlFile);
        final boolean isEmpty = new Yaml().load(inputStream) == null;

        if (inputStream != null) {
            inputStream.close();
        }

        return isEmpty;
    }
}
