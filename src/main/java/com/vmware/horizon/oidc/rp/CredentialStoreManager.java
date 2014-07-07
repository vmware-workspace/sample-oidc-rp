package com.vmware.horizon.oidc.rp;

import com.google.api.client.util.store.FileDataStoreFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class CredentialStoreManager {
    private static final File CREDENTIALS_DIR = new File(System.getProperty("user.home"), ".oauth/google");
    private FileDataStoreFactory dataStoreFactory;

    public CredentialStoreManager() throws IOException {
        dataStoreFactory = new FileDataStoreFactory(CREDENTIALS_DIR);
    }

    public FileDataStoreFactory getDataStoreFactory() {
        return dataStoreFactory;
    }

    public void deleteDataStore() throws IOException {
        FileUtils.deleteDirectory(CREDENTIALS_DIR);
    }
}
