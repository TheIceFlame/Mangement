package com.invoice.mangment.Repository;
import com.invoice.mangment.Entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {

    Optional<Settings> findBySettingKeyAndUserId(String key, Long userId);
}
