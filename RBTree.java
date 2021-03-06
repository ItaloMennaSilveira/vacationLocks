package vacationteste2;

public class RBTree {
    Node root;
    int compID;

    public RBTree() {}

    private Node lookup(int k)
    {
        Node p = root;

        while(p != null) {
            int cmp = compare(k,p.k);
            if(cmp == 0) {
                return p;
            }
            p = (cmp < 0) ? p.l : p.r;
        }

        return null;
    }

    private void rotateLeft(Node x)
    {
        Node r = x.r;
        Node rl = r.l;
        x.r = rl;
        if(rl != null) {
            rl.p = x;
        }

        Node xp = x.p;
        r.p = xp;
        if (xp == null) {
            root = r;
        } else if (xp.l == x) {
            xp.l = r;
        } else {
            xp.r = r;
        }
        r.l = x;
        x.p = r;
    }

    private void rotateRight(Node x)
    {
        Node l = x.l;
        Node lr = l.r;
        x.l = lr;
        if (lr != null) {
            lr.p = x;
        }
        Node xp = x.p;
        l.p = xp;
        if (xp == null) {
            root = l;
        } else if (xp.r == x) {
            xp.r = l;
        } else {
            xp.l = l;
        }

        l.r = x;
        x.p = l;
    }

    private Node parentOf (Node n)
    {
        return ((n!=null) ? n.p : null);
    }

    private Node leftOf (Node n)
    {
        return ((n != null)? n.l : null);
    }

    private Node rightOf(Node n)
    {
        return ((n!= null) ? n.r : null);
    }

    private int colorOf(Node n)
    {
        return ((n!=null) ? n.c : DefinesLock.BLACK);
    }

    private void setColor(Node n, int c)
    {
        if ( n != null) {
            n.c = c;
        }
    }
    
    private void fixAfterInsertion(Node x)
    {
        x.c = DefinesLock.RED;

        while (x != null && x != root) {
            Node xp = x.p;
            if(xp.c != DefinesLock.RED) {
                break;
            }

            if(parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x)));
                if(colorOf(y) == DefinesLock.RED) {
                    setColor(parentOf(x),DefinesLock.BLACK);
                    setColor(y,DefinesLock.BLACK);
                    setColor(parentOf(parentOf(x)),DefinesLock.RED);
                    x = parentOf(parentOf(x));
                } else {
                    if ( x== rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x),DefinesLock.BLACK);
                    setColor(parentOf(parentOf(x)),DefinesLock.RED);
                    if(parentOf(parentOf(x)) != null) {
                        rotateRight(parentOf(parentOf(x)));
                    }
                }
            } else {
                Node y = leftOf(parentOf(parentOf(x)));
                if(colorOf(y) == DefinesLock.RED) {
                    setColor(parentOf(x),DefinesLock.BLACK);
                    setColor(y,DefinesLock.BLACK);
                    setColor(parentOf(parentOf(x)),DefinesLock.RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x),DefinesLock.BLACK);
                    setColor(parentOf(parentOf(x)),DefinesLock.RED);
                    if (parentOf(parentOf(x)) != null) {
                        rotateLeft(parentOf(parentOf(x)));
                    }
                }
            }
        }

        Node ro = root;
        if(ro.c != DefinesLock.BLACK) {
            ro.c = DefinesLock.BLACK;
        }
    }

    private Node insert(int k,Object v,Node n)
    {
        Node t = root;
        if (t== null) {
            if (n == null) {
                return null;
            }
            n.l = null;
            n.r = null;
            n.p = null;
            n.k = k;
            n.v = v;
            n.c = DefinesLock.BLACK;
            root = n;
            return null;
        }

        while(true) {
            int cmp = compare(k,t.k);
            if (cmp == 0) {
                return t;
            } else if (cmp < 0) {
                Node tl = t.l;
                if (tl != null) {
                    t = tl;
                } else {
                    n.l = null;
                    n.r = null;
                    n.k = k;
                    n.v = v;
                    n.p = t;
                    t.l = n;
                    fixAfterInsertion(n);
                    return null;
                }
            } else {
                Node tr = t.r;
                if (tr != null) {
                    t = tr;
                } else {
                    n.l = null;
                    n.r = null;
                    n.k = k;
                    n.v = v;
                    n.p = t;
                    t.r = n;
                    fixAfterInsertion(n);
                    return null;
                }
            }
        }
    }

    private Node successor(Node t)
    {
        if ( t == null) {
            return null;
        } else if( t.r != null) {
            Node p = t.r;
            while (p.l != null) {
                p = p.l;
            }
            return p;
        } else {
            Node p = t.p;
            Node ch = t;
            while (p != null && ch == p.r) {
                ch = p;
                p = p.p;
            }
            return p;
        }

    }

    private void fixAfterDeletion(Node x)
    {
        while (x != root && colorOf(x) == DefinesLock.BLACK) {
            if ( x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));
                if (colorOf(sib) == DefinesLock.RED) {
                    setColor(sib,DefinesLock.BLACK);
                    setColor(parentOf(x),DefinesLock.RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if(colorOf(leftOf(sib)) == DefinesLock.BLACK &&
                        colorOf(rightOf(sib)) == DefinesLock.BLACK) {
                    setColor(sib,DefinesLock.RED);
                    x = parentOf(x);
                } else {
                    if(colorOf(rightOf(sib)) == DefinesLock.BLACK) {
                        setColor(leftOf(sib),DefinesLock.BLACK);
                        setColor(sib,DefinesLock.RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib,colorOf(parentOf(x)));
                    setColor(parentOf(x),DefinesLock.BLACK);
                    setColor(rightOf(sib),DefinesLock.BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Node sib = leftOf(parentOf(x));
                if(colorOf(sib) == DefinesLock.RED) {
                    setColor(sib,DefinesLock.BLACK);
                    setColor(parentOf(x),DefinesLock.RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sib)) == DefinesLock.BLACK &&
                        colorOf(leftOf(sib)) == DefinesLock.BLACK) {
                    setColor(sib,DefinesLock.RED);
                    x = parentOf(x);
                } else {
                    if(colorOf(leftOf(sib)) == DefinesLock.BLACK) {
                        setColor(rightOf(sib), DefinesLock.BLACK);
                        setColor(sib,DefinesLock.RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib,colorOf(parentOf(x)));
                    setColor(parentOf(x),DefinesLock.BLACK);
                    setColor(leftOf(sib),DefinesLock.BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        if (x != null && x.c != DefinesLock.BLACK) {
            x.c = DefinesLock.BLACK;
        }
    }      

    private Node deleteNode(Node p) {
        if(p.l != null && p.r != null) {
            Node s = successor(p);
            p.k = s.k;
            p.v = s.v;
            p = s;
        } 
        
        Node replacement = (p.l != null)? p.l : p.r;

        if (replacement != null) {
            replacement.p = p.p;
            Node pp = p.p;
            if(pp == null) {
                root = replacement;
            } else if( p == pp.l) {
                pp.l = replacement;
            } else {
                pp.r = replacement;
            }

            p.l = null;
            p.r = null;
            p.p = null;


            if(p.c == DefinesLock.BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if(p.p == null) { 
            root = null;
        } else { 
            if (p.c == DefinesLock.BLACK) {
                fixAfterDeletion(p);
            }
            Node pp = p.p;
            if(pp != null) {
                if( p == pp.l) {
                    pp.l = null;
                } else if( p == pp.r) {
                    pp.r = null;
                }
                p.p = null;
            }
        }
        return p;
    }

    private Node firstEntry()
    {
        Node p = root;
        if( p != null) {
            while ( p.l != null) {
                p = p.l;
            }
        }
        return p;
    }

    private int verifyRedBlack(Node root,int depth) 
    {
        int height_left;
        int height_right;

        if ( root == null) {
            return 1;
        }

        height_left = verifyRedBlack(root.l,depth+1);
        height_right = verifyRedBlack(root.r,depth+1);
        if(height_left == 0 || height_right == 0) {
            return 0;
        }
        if (height_left != height_right) {
            System.out.println(" Imbalace @depth = " + depth + " : " + height_left + " " + height_right);
        }

        if (root.l != null && root.l.p != root) {
            System.out.println(" lineage");
        }
        if (root.r != null && root.r.p != root) {
            System.out.println(" lineage");
        }

        /* Red-Black alternation */
        if (root.c == DefinesLock.RED) {
            if (root.l != null && root.l.c != DefinesLock.BLACK) {
                System.out.println("VERIFY in verifyRedBlack");
                return 0;
             }

            if (root.r != null && root.r.c != DefinesLock.BLACK) {
                 System.out.println("VERIFY in verifyRedBlack");   
                 return 0;
            }
            return height_left;
        }
        if(root.c != DefinesLock.BLACK) {
                System.out.println("VERIFY in verifyRedBlack");
                return 0;
        }

        return (height_left + 1);
    }

  private int compare(int a,int b) {
    return a - b;
  }

    public int verify(int verbose) 
    {
        if ( root == null) {
            return 1;
        }
        if(verbose != 0) {
            System.out.println("Integrity check: ");
        }

        if (root.p != null) {
            System.out.println("  (WARNING) root = " + root + " parent = " + root.p);
            return -1;
        }
        if (root.c != DefinesLock.BLACK) {
            System.out.println("  (WARNING) root = " + root + " color = " + root.c);
        }

        int ctr = 0;
        Node its = firstEntry();
        while (its != null) {
            ctr++;
            Node child = its.l;
            if ( child != null && child.p != its) {
                System.out.println("bad parent");
            }
            child = its.r;
            if ( child != null && child.p != its) {
                System.out.println("Bad parent");
            }
            Node nxt = successor(its);
            if (nxt == null) {
                break;
            }
            if( compare(its.k,nxt.k) >= 0) {
                System.out.println("Key order " + its + " ("+its.k+" "+its.v+") " 
                                                + nxt + " ("+nxt.k+" "+nxt.v+") ");
                return -3;
            }
            its =  nxt;
        }

        int vfy = verifyRedBlack(root, 0);
        if(verbose != 0) {
            System.out.println(" Nodes = " + ctr + " Depth = " + vfy);
        }

        return vfy;
    
    }

    public static RBTree alloc(int compID) 
    {
        RBTree n = new RBTree();
        if (n != null) {
            n.compID = compID;
            n.root = null;
        }

        return n;
    }

  public boolean insert(int key,Object val) {
    Node node = new Node();
    Node ex = insert(key,val,node);
    if ( ex != null) {
      node = null;
    }
    return ex == null;
  }

    public boolean remove(int key) 
    {
        Node node = null;
        node = lookup(key);

        if(node != null) {
            node = deleteNode(node);
        }
        return node != null;
    }


    public boolean update(int key,Object val) 
    {
        Node nn = new Node();
        Node ex = insert(key,val,nn);
        if (ex != null) {
            ex.v = val;
            nn = null;
            return true;
        }
        return false;
    }


  public Object find(int key) {
    Node n = lookup(key);
    if (n != null) {
      Object val = n.v;
      return val;
    }
    return null;
  }


    public boolean contains(int key) 
    {
        Node n = lookup(key);

        return (n != null);
    }

}
