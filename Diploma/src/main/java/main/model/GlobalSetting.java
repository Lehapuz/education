package main.model;

import javax.persistence.*;

@Entity
@Table(name = "global_settings")
public class GlobalSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false)
    private GlobalSettingsCode code;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private GlobalSettingsName name;

    @Enumerated(EnumType.STRING)
    @Column(name = "value", nullable = false)
    private GlobalSettingsValue value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GlobalSettingsCode getCode() {
        return code;
    }

    public void setCode(GlobalSettingsCode code) {
        this.code = code;
    }

    public GlobalSettingsName getName() {
        return name;
    }

    public void setName(GlobalSettingsName name) {
        this.name = name;
    }

    public GlobalSettingsValue getValue() {
        return value;
    }

    public void setValue(GlobalSettingsValue value) {
        this.value = value;
    }
}
