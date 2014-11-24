/**
 * 2013年11月20日 上午9:19:22
 */
package cong.commons;

/**
 * @author 刘聪 onion_sheep@163.com|onionsheep@gmail.com
 * cong
 */
public class TestHashCode {

		int i;
		Integer in;
		long l;
		Long lo;
		float f;
		Float fl;
		double d;
		Double dou;
		char c;
		boolean b;
		Boolean bo;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (this.b ? 1231 : 1237);
			result = prime * result + ((this.bo == null) ? 0 : this.bo.hashCode());
			result = prime * result + this.c;
			long temp;
			temp = Double.doubleToLongBits(this.d);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((this.dou == null) ? 0 : this.dou.hashCode());
			result = prime * result + Float.floatToIntBits(this.f);
			result = prime * result + ((this.fl == null) ? 0 : this.fl.hashCode());
			result = prime * result + this.i;
			result = prime * result + ((this.in == null) ? 0 : this.in.hashCode());
			result = prime * result + (int) (this.l ^ (this.l >>> 32));
			result = prime * result + ((this.lo == null) ? 0 : this.lo.hashCode());
			return result;
		}
		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			final TestHashCode other = (TestHashCode) obj;
			if (this.b != other.b) {
				return false;
			}
			if (this.bo == null) {
				if (other.bo != null) {
					return false;
				}
			} else if (!this.bo.equals(other.bo)) {
				return false;
			}
			if (this.c != other.c) {
				return false;
			}
			if (Double.doubleToLongBits(this.d) != Double.doubleToLongBits(other.d)) {
				return false;
			}
			if (this.dou == null) {
				if (other.dou != null) {
					return false;
				}
			} else if (!this.dou.equals(other.dou)) {
				return false;
			}
			if (Float.floatToIntBits(this.f) != Float.floatToIntBits(other.f)) {
				return false;
			}
			if (this.fl == null) {
				if (other.fl != null) {
					return false;
				}
			} else if (!this.fl.equals(other.fl)) {
				return false;
			}
			if (this.i != other.i) {
				return false;
			}
			if (this.in == null) {
				if (other.in != null) {
					return false;
				}
			} else if (!this.in.equals(other.in)) {
				return false;
			}
			if (this.l != other.l) {
				return false;
			}
			if (this.lo == null) {
				if (other.lo != null) {
					return false;
				}
			} else if (!this.lo.equals(other.lo)) {
				return false;
			}
			return true;
		}



}

