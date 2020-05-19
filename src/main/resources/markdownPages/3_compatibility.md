# Compatibility

This website will try to advertise planned future changes that could break
[backward compatibility](https://en.wikipedia.org/wiki/Backward_compatibility) (referred to by "breaking changes" further) with the clients that uses it.

This website will try to warn machine type clients of imminent breaking changes.
To do so, this website might respond to HTTP requests with a Sunset and/or Deprecation HTTP header 7 calender days before the breaking change is introduced.
If this is done, there will probably be a Link HTTP header containing a link to a deprecation or sunset documentation web page (which might be this page).
Future breaking changes might be listed on this web page.
If you develop/create machine type client(s) that accesses this website through HTTP, please make sure you have a system in place to automatically warn
you of future breaking changes by looking up the HTTP headers mentioned above (and read the RFCs below) and please update your software to adapt to the breaking
change before it occurs (the likely date of the breaking change will be available from the HTTP header value); or accept the impact that the breaking change might/will have.
Also, I will assume that only human type clients will access HTML content.
Here are the RFCs that this website will follow to notify of breaking changes to machine type clients:

- [RFC 8594: Sunset HTTP Header](https://tools.ietf.org/html/rfc8594)
- [Deprecation HTTP Header draft version 2](https://tools.ietf.org/html/draft-dalal-deprecation-header-02)

When a breaking change is introduced, a notice to human type clients will probably be added as a header section at the top of a web page to
instructs human type clients on how to proceed given the new version of the website.
This notice might only be placed once the new version of the website, containing the breaking change, is deployed.
