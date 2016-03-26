package io.proffitt.coherence.settings;

public class Value {
	public static final int	TYPE_LONG	= 0;
	public static final int	TYPE_DOUBLE	= 1;
	public static final int	TYPE_STRING	= 2;
	public static final int	TYPE_BOOL	= 3;
	private ValueOwner		OWNER		= null;
	private String			ID			= null;
	long					lVal;
	double					dVal;
	String					sVal;
	boolean					bVal;
	int						type;
	public Value(Value v) {
		type = v.type;
		lVal = v.lVal;
		dVal = v.dVal;
		sVal = v.sVal;
		bVal = v.bVal;
	}
	public Value(long v) {
		setLong(v);
	}
	public Value(double v) {
		setDouble(v);
	}
	public Value(String v) {
		setString(v);
	}
	public Value(boolean v) {
		setBool(v);
	}
	public String toString(){
		return getString();
	}
	void setOwner(ValueOwner vo, String id) {
		if (OWNER != null || ID != null) {
			throw new RuntimeException("Value is already owned!");
		} else {
			OWNER = vo;
			ID = id;
		}
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof Value) {
			return ((Value) o).type == this.type && ((Value) o).getString().equals(this.getString());
		} else if (o instanceof String && type == TYPE_STRING) {
			return sVal.equals(o);
		}
		return o.toString().equals(this.getString());
	}
	public int getType() {
		return type;
	}
	public void parse() {
		if (type == TYPE_STRING) {
			if (sVal.equalsIgnoreCase("true")) {
				type = TYPE_BOOL;
				bVal = true;
			} else if (sVal.equalsIgnoreCase("false")) {
				type = TYPE_BOOL;
				bVal = false;
			} else {
				try {
					long l = Long.parseLong(sVal);
					type = TYPE_LONG;
					lVal = l;
				} catch (NumberFormatException e1) {
					try {
						double d = Double.parseDouble(sVal);
						type = TYPE_DOUBLE;
						dVal = d;
					} catch (NumberFormatException e2) {
						return;
					}
				}
			}
		}
		if (OWNER != null) {
			OWNER.alert(ID);
		}
	}
	public void set(Value rhs) {
		this.type = rhs.type;
		switch (type) {
		case 0:
			lVal = rhs.lVal;
			break;
		case 1:
			dVal = rhs.dVal;
			break;
		case 2:
			sVal = rhs.sVal;
			break;
		case 3:
			bVal = rhs.bVal;
			break;
		default:
			break;
		}
		if (OWNER != null) {
			OWNER.alert(ID);
		}
	}
	public void setLong(long v) {
		type = TYPE_LONG;
		lVal = v;
		if (OWNER != null) {
			OWNER.alert(ID);
		}
	}
	public void setDouble(double v) {
		type = 1;
		dVal = v;
		if (OWNER != null) {
			OWNER.alert(ID);
		}
	}
	public void setString(String v) {
		type = 2;
		sVal = v;
		if (OWNER != null) {
			OWNER.alert(ID);
		}
	}
	public void setBool(boolean v) {
		type = 3;
		bVal = v;
		if (OWNER != null) {
			OWNER.alert(ID);
		}
	}
	public String getString() {
		switch (type) {
		case 0:
			return String.valueOf(lVal);
		case 1:
			return String.valueOf(dVal);
		case 2:
			return sVal;
		case 3:
			return String.valueOf(bVal);
		default:
			return null;//ERROR
		}
	}
	public long getLong() {
		switch (type) {
		case 0:
			return lVal;
		case 1:
			return (long) dVal;
		case 2:
			return Long.parseLong(sVal);
		case 3:
			return (bVal ? 1 : 0);
		default:
			return 0;//ERROR
		}
	}
	public double getDouble() {
		switch (type) {
		case 0:
			return lVal;
		case 1:
			return dVal;
		case 2:
			return Double.parseDouble(sVal);
		case 3:
			return (bVal ? 1 : -1);
		default:
			return 0;//ERROR
		}
	}
	public boolean getBool() {
		switch (type) {
		case 0:
			return !(lVal >= 0);
		case 1:
			return dVal >= 0;
		case 2:
			return Boolean.parseBoolean(sVal);
		case 3:
			return bVal;
		default:
			return false;//ERROR
		}
	}
	//convenience methods below
	public void setInt(int v) {
		setLong(v);
	}
	public void setFloat(float v) {
		setDouble(v);
	}
	public int getInt() {
		return (int) getLong();
	}
	public float getFloat() {
		return (float) getDouble();
	}
	//operators
	public void Add(Value rhs) {
		if (type == TYPE_STRING || rhs.type == TYPE_STRING) {
			setString(getString() + rhs.getString());
		} else if (type == TYPE_DOUBLE || rhs.type == TYPE_DOUBLE) {
			setDouble(getDouble() + rhs.getDouble());
		} else if (type == TYPE_LONG || rhs.type == TYPE_LONG) {
			setLong(getLong() + rhs.getLong());
		} else if (type == TYPE_BOOL && rhs.type == TYPE_BOOL) {
			setBool(getBool() || rhs.getBool());
		} else {
			//IMPOSSIBLE!
			throw new RuntimeException("Could not add values.");
		}
	}
	public void Subtract(Value rhs) {
		if (type == TYPE_STRING || rhs.type == TYPE_STRING) {
			throw new RuntimeException("Cannot subtract Strings.");
		} else if (type == TYPE_DOUBLE || rhs.type == TYPE_DOUBLE) {
			setDouble(getDouble() - rhs.getDouble());
		} else if (type == TYPE_LONG || rhs.type == TYPE_LONG) {
			setLong(getLong() - rhs.getLong());
		} else if (type == TYPE_BOOL && rhs.type == TYPE_BOOL) {
			setBool(getBool() && rhs.getBool());
		} else {
			//IMPOSSIBLE!
			throw new RuntimeException("Could not subtract values.");
		}
	}
	public void Multiply(Value rhs) {
		if (type == TYPE_STRING || rhs.type == TYPE_STRING) {
			throw new RuntimeException("Cannot multiply Strings.");
		} else if (type == TYPE_DOUBLE || rhs.type == TYPE_DOUBLE) {
			setDouble(getDouble() * rhs.getDouble());
		} else if (type == TYPE_LONG || rhs.type == TYPE_LONG) {
			setLong(getLong() * rhs.getLong());
		} else if (type == TYPE_BOOL && rhs.type == TYPE_BOOL) {
			setBool(getBool() && rhs.getBool());
		} else {
			//IMPOSSIBLE!
			throw new RuntimeException("Could not multiply values");
		}
	}
	public void Divide(Value rhs) {
		if (type == TYPE_STRING || rhs.type == TYPE_STRING) {
			throw new RuntimeException("Cannot divide Strings.");
		} else if (type == TYPE_DOUBLE || rhs.type == TYPE_DOUBLE) {
			setDouble(getDouble() / rhs.getDouble());
		} else if (type == TYPE_LONG || rhs.type == TYPE_LONG) {
			setLong(getLong() / rhs.getLong());
		} else if (type == TYPE_BOOL && rhs.type == TYPE_BOOL) {
			setBool(getBool() ^ rhs.getBool());
		} else {
			//IMPOSSIBLE!
			throw new RuntimeException("Could not divide values.");
		}
	}
}
