namespace SampleApi;

// Small piece of domain logic so the pipeline has something real to test.
public static class Greeter
{
    public static string Greet(string? name)
    {
        var who = string.IsNullOrWhiteSpace(name) ? "world" : name.Trim();
        return $"Hello, {who}!";
    }
}
