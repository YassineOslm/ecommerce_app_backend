package eafc.uccle.be.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LivesId implements Serializable {

    private Long userId;
    private Long userAddressId;

    public LivesId() {}

    public LivesId(Long userId, Long userAddressId) {
        this.userId = userId;
        this.userAddressId = userAddressId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(Long userAddressId) {
        this.userAddressId = userAddressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LivesId that = (LivesId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(userAddressId, that.userAddressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userAddressId);
    }
}
