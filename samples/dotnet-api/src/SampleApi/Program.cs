using SampleApi;

var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

app.MapGet("/health", () => Results.Ok(new { status = "ok" }));
app.MapGet("/hello", (string? name) => Results.Ok(new { message = Greeter.Greet(name) }));

app.Run();

// Exposed for integration tests
public partial class Program { }
