using System.Security.Cryptography;
using System.Text;

namespace PizzaTime.Data;
public static class Crypto
{
    public static byte[] ToSHA512(this string text)
    {
        return SHA512.HashData(Encoding.UTF8.GetBytes(text));
    }

    public static string ToHashedString(this byte[] hash){
        return Encoding.UTF8.GetString(hash);
    }
}
