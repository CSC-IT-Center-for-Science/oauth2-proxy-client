import argparse
from uuid import uuid4
from time import sleep
import urllib2

def main():

    parser = argparse.ArgumentParser()
    parser.add_argument('--hostname', type=str, default='http://localhost:8080',
                        help='AAI proxy service hostname')
    args = parser.parse_args()

    unique = str(uuid4())
    url = 'http://%s/register?unique=%s' % (args.hostname, unique)

    print 'To access FUSE filesystem please log in to ELIXIR AAI using url: %s' % url

    unique_url = 'http://%s/unique/%s' % (args.hostname, unique)
    while True:
        sleep(5)
        response = urllib2.urlopen(unique_url).read()

        if response:
            print '==========='
            print 'Got response from AAI proxy, mounting the filesystem...'
            print 'java -jar ~/APIFUSE/target/EgaFUSE-1.0-SNAPSHOT.jar -t %s -m ~/fuse &' % response
            break

if __name__ == "__main__":
        main()
