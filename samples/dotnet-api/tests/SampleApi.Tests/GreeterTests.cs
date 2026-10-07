using SampleApi;
using Xunit;

namespace SampleApi.Tests;

public class GreeterTests
{
    [Theory]
    [InlineData("Xavi", "Hello, Xavi!")]
    [InlineData("  Ana ", "Hello, Ana!")]
    [InlineData(null, "Hello, world!")]
    [InlineData("", "Hello, world!")]
    public void Greet_ReturnsExpectedMessage(string? name, string expected)
    {
        Assert.Equal(expected, Greeter.Greet(name));
    }
}
