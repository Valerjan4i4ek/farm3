public enum SIGN
{
    EMPTY("_"),
    PLANT("X"),//семена
    HARVEST("0");//урожай

    private final String symbol;

    private SIGN(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol()
    {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public SIGN updateSign(){
        final SIGN[] signs = SIGN.values();
        return signs.length <= this.ordinal() + 1 ? signs[0] : signs[this.ordinal() + 1];
    }

    public static SIGN valueOfDesc(String symbol)
    {
        for(SIGN e: SIGN.values())
        {
            if(e.symbol.equals(symbol))
            {
                return e;
            }
        }
        return null;
    }
}
