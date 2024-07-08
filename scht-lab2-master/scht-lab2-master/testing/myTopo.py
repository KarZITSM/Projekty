from mininet.topo import Topo

class myTopo(Topo):

    def __init__(self):
        Topo.__init__(self)

        Berlin = self.addHost('h1')
        Vienna = self.addHost('h2')
        Warsaw = self.addHost('h3')
        Vilnius = self.addHost('h4')
        Amsterdam = self.addHost('h5')
        Nantes = self.addHost('h6')
        Florence = self.addHost('h7')
        Sofia = self.addHost('h8')
        Lviv = self.addHost('h9')
        Copenhagen = self.addHost('h10')

        s1 = self.addSwitch('s1')
        s2 = self.addSwitch('s2')
        s3 = self.addSwitch('s3')
        s4  =self.addSwitch('s4')
        s5 = self.addSwitch("s5")
        s6 = self.addSwitch("s6")
        s7 = self.addSwitch("s7")
        s8 = self.addSwitch("s8")
        s9 = self.addSwitch("s9")
        s10 = self.addSwitch("s10")

        self.addLink(Berlin, s1, delay='0.2ms', bw=800, max_queue_size=300)
        self.addLink(Vienna, s2, delay='0.2ms', bw=800, max_queue_size=300)
        self.addLink(Warsaw, s3, delay='0.2ms', bw=800, max_queue_size=300)
        self.addLink(Vilnius, s4, delay='0.2ms', bw=800, max_queue_size=300)
        self.addLink(Amsterdam, s5, delay='0.2ms', bw=800, max_queue_size=300)
        self.addLink(Nantes, s6, delay='0.2ms', bw=800, max_queue_size=300)
        self.addLink(Florence, s7, delay='0.2ms', bw=800, max_queue_size=300)
        self.addLink(Sofia, s8, delay='0.2ms', bw=800, max_queue_size=300)
        self.addLink(Lviv, s9, delay='0.2ms', bw=800, max_queue_size=300)
        self.addLink(Copenhagen, s10, delay='0.2ms', bw=800, max_queue_size=300)

        self.addLink(s1, s10, delay='2.49ms', bw=800, loss=2.0, max_queue_size=2000)
        self.addLink(s1, s2, delay='3.57ms', bw=800, loss=2.0, max_queue_size=5000)
        self.addLink(s1, s3, delay='3.68ms', bw=800, loss=2.0, max_queue_size=5000)
        self.addLink(s1, s5, delay='4.04ms', bw=800, loss=2.0, max_queue_size=4000)
        self.addLink(s3, s4, delay='2.76ms', bw=800, loss=2.0, max_queue_size=2000)
        self.addLink(s3, s9, delay='2.4ms', bw=800, loss=2.0, max_queue_size=2000)
        self.addLink(s2, s8, delay='5.79ms', bw=800, loss=2.0, max_queue_size=2000)
        self.addLink(s2, s7, delay='4.45ms', bw=800, loss=2.0, max_queue_size=1500)
        self.addLink(s5, s6, delay='5.22ms', bw=800, loss=2.0, max_queue_size=1000)

topos={'EuropeTopo':(lambda:myTopo())}