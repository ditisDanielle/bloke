package nl.mad.bacchus.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import nl.mad.bacchus.model.Photo;
import nl.mad.bacchus.model.Product;
import nl.mad.bacchus.model.Wine;
import org.springframework.stereotype.Component;

@Component
public class PhotoBuilder extends AbstractBuilder {

    public PhotoBuildCommand newPhoto(Product product) {
		return new PhotoBuildCommand(product);
	}

	public class PhotoBuildCommand {

        private Product product;
		private byte[] data;
		private String contentType;

        public PhotoBuildCommand(Product product) {
            this.product = product;
		}

		public PhotoBuildCommand fromTestResource(String name) {
			InputStream in = getClass().getResourceAsStream(name);
			if (in == null) {
				throw new IllegalArgumentException(name + " not found.");
			}
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[8192];
				try {
					int rd = -1;
					while ((rd = in.read(buffer)) >= 0) {
						out.write(buffer, 0, rd);
					}
				} finally {
					in.close();
				}
				data = out.toByteArray();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			if (name.endsWith("jpg")||name.endsWith(".jpeg")) {
				contentType="image/jpeg";
			}
			if (name.endsWith(".gif")) {
				contentType="image/gif";
			}
			if(name.endsWith(".png")) {
				contentType="image/png";
			}
			if (contentType ==null) {
				throw new IllegalArgumentException(name+" - unknown content type.");
			}
			return this;
		}

		/**
		 * Build the photo.
		 * 
		 * @return the created photo
		 */
		public Photo build() {
            return new Photo(data, product, contentType);
		}

		/**
		 * Persists the photo.
		 * 
		 * @return the persisted photo
		 */
		public Photo save() {
			return saveWithTransaction(build());
		}

	}

}
