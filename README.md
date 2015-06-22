# DNSWanIP
DNSWanIP was created to fill a simple problem:
How do I share my website on my home server when I have an ISP that charges waaayyy too much for a static (business) IP address?

I didn't want to pay for hosting and I didn't want to pay for NoIP or a similar service, all thought it is cheap to do that.
This was more an excersize to see if I could figure out a way to do this on my own.

The solution that I came up with is many fold and this program is the end user part of that solution.
Basically the setup is to use Dropbox to sync your IP address every 15 minutes and use this end user program as a one
click hyperlink to take individuals to your site.

The concept is pretty simple:
Use curl to save the current IP to a file
The file is synced with dropbox.
The app grabs the raw dropbox file content and creates a link based on the IP saved in that file.  The link is then automatically ran.

The best thing about this is you can set up a direct IP link only once.




Future Plans:
Create a desktop app for the same
if possible, use a free website service to redirect to current site
