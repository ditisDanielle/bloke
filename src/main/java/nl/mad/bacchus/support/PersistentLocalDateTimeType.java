package nl.mad.bacchus.support;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;

public class PersistentLocalDateTimeType extends UserTypeSupport implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.TIMESTAMP };
    }

    @Override
    public Class<?> returnedClass() {
        return LocalDateTime.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        Timestamp timestamp = rs.getTimestamp(names[0]);
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        LocalDateTime time = (LocalDateTime) value;
        if (time != null) {
            st.setTimestamp(index, Timestamp.valueOf(time));
        } else {
            st.setNull(index, Types.TIMESTAMP);
        }
    }

}
